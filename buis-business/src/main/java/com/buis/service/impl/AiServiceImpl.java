package com.buis.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.buis.common.exception.ServiceException;
import com.buis.common.manager.RedisLimiterManager;
import com.buis.common.utils.StringUtils;
import com.buis.reponse.AuthResponse;
import com.buis.service.IAiService;
import io.netty.channel.ChannelOption;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Service
@ConfigurationProperties(prefix = "aliyun.ai")
@Slf4j
public class AiServiceImpl implements IAiService {
    @Resource
    private RedissonClient redissonClient;


    private final Logger logger = LoggerFactory.getLogger(AiServiceImpl.class);
    private static StringBuilder reasoningContent = new StringBuilder(); //思考内容
    private static StringBuilder finalContent = new StringBuilder(); //最终回复内容
    private static boolean isFirstPrint = true;
    private static final String REASONING_SECTION = "[SECTION-1]====================思考过程====================";
    private static final String CONTENT_SECTION = "[SECTION-2]====================完整回复====================";

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
        RedisLimiterManager redisLimiterManager = new RedisLimiterManager();
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



}

