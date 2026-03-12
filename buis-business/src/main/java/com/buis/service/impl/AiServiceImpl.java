package com.buis.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.buis.common.exception.ServiceException;
import com.buis.common.manager.RedisLimiterManager;
import com.buis.common.utils.StringUtils;
import com.buis.mapper.SkillTagMapper;
import com.buis.model.domain.SkillTag;
import com.buis.model.dto.SkillExtractRequest;
import com.buis.model.dto.SkillExtractResult;
import com.buis.reponse.AuthResponse;
import com.buis.service.IAiService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import javax.annotation.Resource;
import java.rmi.ServerException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
@ConfigurationProperties(prefix = "aliyun.ai")
@Slf4j
public class AiServiceImpl implements IAiService {

    @Resource
    private RedisLimiterManager redisLimiterManager;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SkillTagMapper skillTagMapper;


    private final Logger logger = LoggerFactory.getLogger(AiServiceImpl.class);
    private static StringBuilder reasoningContent = new StringBuilder(); //思考内容
    private static StringBuilder finalContent = new StringBuilder(); //最终回复内容
    private static boolean isFirstPrint = true;
    private static final String REASONING_SECTION = "[SECTION-1]====================思考过程====================";
    private static final String CONTENT_SECTION = "[SECTION-2]====================完整回复====================";
    /**
     * AI提取技能关键词的提示词模板
     */
    private static final String SKILL_EXTRACT_PROMPT =
            "你是一个专业的技能分析专家。请仔细分析以下文本，从中提取用户可能具备的技能标签。\n\n" +
                    "分析文本：\n" +
                    "%s\n\n" +
                    "请按照以下步骤进行分析：\n" +
                    "1. 首先，识别文本中明确提到的技能（如'精通Java'、'熟悉Spring Boot'）\n" +
                    "2. 然后，从用户的经历、职责、项目描述中推断可能相关的技能\n" +
                    "3. 最后，综合考虑工具使用、方法论、相关领域知识\n\n" +
                    "提取原则：\n" +
                    "- 优先提取技术类技能（编程语言、框架、工具等）\n" +
                    "- 其次提取软技能（沟通、协作、项目管理等）\n" +
                    "- 避免提取过于宽泛的词语（如'计算机'、'技术'）\n" +
                    "- 每个技能词尽量简洁、具体\n\n" +
                    "返回格式要求：\n" +
                    "请返回一个JSON数组，格式示例：[\"Java\", \"Spring Boot\", \"MySQL\", \"团队协作\", \"项目管理\"]\n\n" +
                    "如果确实没有识别到任何技能，返回空数组[]。";

    @Value("${aliyun.ai.api-key}")
    String apiKey;
    @Value("${aliyun.ai.base-url}")
    String baseUrl;

    @Value("${wenxin.app.id}")
    String appId; //文心智能体id
    @Value("${wenxin.app.secret.key}")
    String secretKey; //文心智能体密钥
    @Value("${wenxin.agent.api-url}")
    String agentApiUrl; //文心获取token的url

    private Flux<String> handleGenerationResult(GenerationResult result, AtomicBoolean isFirstReasoning, AtomicBoolean isFirstContent) {
        List<String> events = new ArrayList<>();

        // 处理思考过程
        String reasoning = result.getOutput().getChoices().get(0).getMessage().getReasoningContent();
        if (StringUtils.hasText(reasoning)) {
            // 如果是第一次发送思考内容，先插入分隔符
            if (isFirstReasoning.compareAndSet(true, false)) {
                events.add(REASONING_SECTION);
            }
            events.add(reasoning);
        }

        // 处理完整回复
        String content = result.getOutput().getChoices().get(0).getMessage().getContent();
        if (StringUtils.hasText(content)) {
            // 如果是第一次发送完整回复，先插入分隔符
            if (isFirstContent.compareAndSet(true, false)) {
                events.add(CONTENT_SECTION);
            }
            events.add(content);
        }

        return Flux.fromIterable(events);
    }

    /**
     * 构建参数对象
     * @param userMsg 用户消息对象(输入信息,用户角色等待)
     * @return
     */
    private GenerationParam buildGenerationParam(Message userMsg) {
        return GenerationParam.builder()
                // 若没有配置环境变量，请用百炼API Key将下行替换为：.apiKey("sk-xxx")
                .apiKey(apiKey)
                .model("deepseek-r1")
                .messages(Arrays.asList(userMsg))
                // 不可以设置为"text"
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .incrementalOutput(true)
                .build();
    }

    @Override
    public Flux<String> streamCallWithMessage(Generation gen, Message userMsg)
            throws NoApiKeyException, ApiException, InputRequiredException {
        // 每个请求独立维护状态（避免并发问题）
        AtomicBoolean isFirstReasoning = new AtomicBoolean(true);
        AtomicBoolean isFirstContent = new AtomicBoolean(true);
        // 构建参数
        GenerationParam param = buildGenerationParam(userMsg);
//        // 调用
//        Flowable<GenerationResult> resultFlowable = gen.streamCall(param);
//        //result流式输出
//        Flowable<String> stringFlowable = resultFlowable.flatMap(AiServiceImpl::handleGenerationResult);
//        Flux<String> flux = Flux.from(stringFlowable);
//        return flux;
// 2. 调用流式接口，转换为 Reactor 的 Flux 并处理背压
        return Flux.from(gen.streamCall(param))
                // 3. 扁平化处理每个 GenerationResult 生成多个事件
                .concatMap(result -> handleGenerationResult(result, isFirstReasoning, isFirstContent).onBackpressureBuffer())
                // 4. 统一错误处理
                .onErrorResume(e -> {
                    log.error("Stream call failed", e);
                    return Flux.just("[ERROR]" + e.getMessage());
                });


    }

    /**
     * 文心智能体调用
     *
     * @param message
     * @param threadId
     * @return
     */
    @Override
    public Flux<String> agentCallWithMessage(String message, String threadId) {
        //获取登录用户id
        String loginIdStr = StpUtil.getLoginId().toString();
        long loginId;
        try {
            loginId = Long.parseLong(loginIdStr);
        } catch (NumberFormatException e) {
            throw new ServiceException("无效的用户ID格式");
        }
        // 限流判断，每个用户一个限流器
        redisLimiterManager.doRateLimit("chatWithAgent_" + loginId);
        RestTemplate restTemplate = new RestTemplate();
        // 1. 获取 access_token（增强错误处理）
        String authUrl = String.format("%s?grant_type=client_credentials&client_id=%s&client_secret=%s",
                agentApiUrl, appId, secretKey);
        // 打印调试信息
        log.info("请求Token URL: {}", authUrl);

        try {
            assert authUrl != null;
            ResponseEntity<AuthResponse> response = restTemplate.getForEntity(authUrl, AuthResponse.class);
            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                return Flux.error(new RuntimeException("获取Token失败: " + response.getStatusCode()));
            }
            String accessToken = response.getBody().getAccess_token();
            log.info("成功获取Token: {}", accessToken.substring(0, 6) + "****"); // 安全日志

            // 2. 构建请求体（修正字段）
            Map<String, Object> requestBody = new HashMap<>();
            Map<String, Object> contentValue = new HashMap<>();
            contentValue.put("showText", message);

            Map<String, Object> messageContent = new HashMap<>();
            messageContent.put("type", "text");
            messageContent.put("value", contentValue);

            requestBody.put("message", Collections.singletonMap("content", messageContent));
            requestBody.put("source", appId); // 根据文档填写固定值
            requestBody.put("from", "openapi");
            requestBody.put("openId", Long.toString(loginId)); //用户id
//            requestBody.put("stream", true);

            // 3. 使用正确API地址和鉴权方式
            WebClient client = WebClient.builder()
                    .baseUrl("https://agentapi.baidu.com/assistant/conversation?appId=" + appId + "&secretKey=" + secretKey)
                    .defaultHeader("Authorization", "Bearer " + accessToken)
                    .clientConnector(new ReactorClientHttpConnector( //连接池
                            HttpClient.create()
                                    .responseTimeout(Duration.ofSeconds(30)) // 设置响应超时
                                    .option(ChannelOption.SO_KEEPALIVE, true)
                    ))
                    .build();

            return client.post()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("access_token", accessToken) // 正确传递token
                            .build())
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .onStatus(HttpStatus::is4xxClientError,
                            clientResponse -> Mono.error(new RestClientException("客户端错误")))
                    .onStatus(HttpStatus::is5xxServerError,
                            clientResponse -> Mono.error(new ServerException("服务端错误")))
                    .bodyToFlux(String.class)
                    .onErrorResume(e -> {
                        log.error("Agent call failed", e);
                        return Flux.just("[ERROR]" + e.getMessage());
                    }); // 直接返回字符串流

        } catch (Exception e) {
            return Flux.error(new RuntimeException("认证流程失败: " + e.getMessage()));
        }
    }

    @Override
    public SkillExtractResult extractSkillsFromText(SkillExtractRequest request) {
        String text = request.getText();
        String type = request.getType();

        if (!StringUtils.hasText(text)) {
            return SkillExtractResult.builder()
                    .matchedSkills(Collections.emptyList())
                    .unmatchedKeywords(Collections.emptyList())
                    .status("failed")
                    .message("文本内容为空")
                    .fallbackUsed(false)
                    .build();
        }

        try {
            // 尝试调用AI接口提取技能
            List<String> extractedKeywords = extractSkillsWithAI(text);

            // 如果没有提取到关键词，尝试使用降级方案
            if (CollectionUtils.isEmpty(extractedKeywords)) {
                log.warn("AI提取技能失败，使用关键词匹配降级方案");
                extractedKeywords = extractSkillsWithKeywordMatching(text);
                return matchKeywordsToTags(extractedKeywords, true);
            }

            return matchKeywordsToTags(extractedKeywords, false);

        } catch (Exception e) {
            log.error("AI提取技能异常，使用降级方案", e);
            // AI调用失败，使用降级方案
            List<String> keywords = extractSkillsWithKeywordMatching(text);
            return matchKeywordsToTags(keywords, true);
        }
    }
    /**
     * 调用AI接口提取技能关键词
     */
    private List<String> extractSkillsWithAI(String text) throws NoApiKeyException, ApiException, InputRequiredException {
        Generation gen = new Generation();
        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content(String.format(SKILL_EXTRACT_PROMPT, text))
                .build();

        GenerationParam param = GenerationParam.builder()
                .apiKey(apiKey)
                .model("deepseek-r1") // 或使用其他模型
                .messages(Arrays.asList(userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();

        try {
            GenerationResult result = gen.call(param);
            String content = result.getOutput().getChoices().get(0).getMessage().getContent();

            // 解析JSON数组
            JsonNode jsonNode = objectMapper.readTree(content);
            if (jsonNode.isArray()) {
                List<String> keywords = new ArrayList<>();
                for (JsonNode node : jsonNode) {
                    keywords.add(node.asText());
                }
                return keywords;
            }
        } catch (Exception e) {
            log.error("解析AI响应失败", e);
        }

        return Collections.emptyList();
    }
    /**
     * 关键词匹配降级方案
     */
    private List<String> extractSkillsWithKeywordMatching(String text) {
        // 从数据库中获取所有技能标签作为关键词库
        List<SkillTag> allTags = skillTagMapper.selectAllActiveTags();
        List<String> keywords = new ArrayList<>();
        String lowerText = text.toLowerCase();

        for (SkillTag tag : allTags) {
            if (lowerText.contains(tag.getTagName().toLowerCase())) {
                keywords.add(tag.getTagName());
            }
        }

        return keywords;
    }
    /**
     * 将关键词匹配到标准技能标签
     */
    private SkillExtractResult matchKeywordsToTags(List<String> keywords, boolean isFallback) {
        if (CollectionUtils.isEmpty(keywords)) {
            return SkillExtractResult.builder()
                    .matchedSkills(Collections.emptyList())
                    .unmatchedKeywords(Collections.emptyList())
                    .status(isFallback ? "failed" : "success")
                    .message(isFallback ? "未匹配到任何技能" : "成功提取但无匹配技能")
                    .fallbackUsed(isFallback)
                    .build();
        }

        // 查询所有启用的技能标签
        List<SkillTag> allTags = skillTagMapper.selectAllActiveTags();
        Map<String, SkillTag> tagMap = allTags.stream()
                .collect(Collectors.toMap(SkillTag::getTagName, tag -> tag, (t1, t2) -> t1));

        List<SkillTag> matchedSkills = new ArrayList<>();
        List<String> unmatchedKeywords = new ArrayList<>();

        for (String keyword : keywords) {
            // 精确匹配
            if (tagMap.containsKey(keyword)) {
                matchedSkills.add(tagMap.get(keyword));
            } else {
                // 模糊匹配（可优化为相似度匹配）
                Optional<SkillTag> fuzzyMatch = allTags.stream()
                        .filter(tag -> tag.getTagName().toLowerCase().contains(keyword.toLowerCase()) ||
                                keyword.toLowerCase().contains(tag.getTagName().toLowerCase()))
                        .findFirst();

                if (fuzzyMatch.isPresent()) {
                    matchedSkills.add(fuzzyMatch.get());
                } else {
                    unmatchedKeywords.add(keyword);
                }
            }
        }

        String status = matchedSkills.isEmpty() ? "failed" :
                !unmatchedKeywords.isEmpty() ? "partial" : "success";

        return SkillExtractResult.builder()
                .matchedSkills(matchedSkills)
                .unmatchedKeywords(unmatchedKeywords)
                .status(status)
                .message(String.format("匹配到%d个技能，%d个未匹配", matchedSkills.size(), unmatchedKeywords.size()))
                .fallbackUsed(isFallback)
                .build();
    }






    }

