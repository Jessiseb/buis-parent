package com.buis.service.impl;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.buis.common.utils.StringUtils;
import com.buis.reponse.AuthResponse;
import com.buis.service.IAiService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@ConfigurationProperties(prefix = "aliyun.ai")
@Slf4j
public class AiServiceImpl implements IAiService {
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

    @Value("${wenxin.api.id}")
    String apiId; //文心智能体id
    @Value("${wenxin.api.secret.key}")
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

    @Override
    public Flux<ServerSentEvent<String>> agentCallWithMessage(String messageContent, String threadId) {
        //获取到token
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("%s?grant_type=client_credentials&client_id=%s&client_secret=%s",
                agentApiUrl, apiId, secretKey);
        //设置请求方式为application/json

        System.out.println(apiId);
        System.out.println(secretKey);
        ResponseEntity<AuthResponse> response = restTemplate.getForEntity(url, AuthResponse.class);
        String accessToken = response.getBody().getAccess_token();

        WebClient client = WebClient.builder().build();

        // 构造请求体
        String requestBody = String.format(
                "{\"threadId\": \"%s\", \"message\": {\"content\": {\"type\": \"text\", \"value\": \"%s\"}}, " +
                        "\"source\": \"%s\", \"from\": \"openapi\", \"openId\": \"%s\"}",
                threadId, messageContent, apiId, "123"
        );


        return client.post()
                .uri("https://agentapi.baidu.com/assistant/conversation?appId=%s&secretKey=%s",apiId,secretKey)
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToFlux(String.class)
                .map(data -> ServerSentEvent.<String>builder().data(data).build());
    }


}
