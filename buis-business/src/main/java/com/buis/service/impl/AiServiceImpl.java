package com.buis.service.impl;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.buis.common.utils.StringUtils;
import com.buis.service.IAiService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
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

//        private static Flux<String> handleGenerationResult(GenerationResult message) {
//        // 获取生成消息中的思考内容
//        String reasoning = message.getOutput().getChoices().get(0).getMessage().getReasoningContent();
//        // 获取生成消息中的回复内容
//        String content = message.getOutput().getChoices().get(0).getMessage().getContent();
//
//        Flux<String> reasoningFlux = Flux.empty();
//        Flux<String> contentFlux = Flux.empty();
//
//
//        // 如果思考内容不为空，则将其追加到思考内容缓存中，并根据isFirstPrint标志决定是否打印思考过程的标题
//        if (!reasoning.isEmpty()) {
//            reasoningContent.append(reasoning);
//            if (isFirstPrint) {
//                System.out.println("====================思考过程====================");
//                isFirstPrint = false;
//            }
//            System.out.print(reasoning);
//            // 添加标识 [REASONING]
//            reasoningFlux = Flux.just( reasoning);
////            return Flux.create(sink -> {
////                //每次的回复都带有 "思考过程",然后返回给流
////                reasoningContent.append("\n====================思考过程====================\n");
////                sink.next(reasoning);
////                sink.complete();
////            });
//
//            //流式返回
//
//
//        }
//        // 如果回复内容不为空，则将其追加到最终内容缓存中，并根据isFirstPrint标志决定是否打印完整回复的标题
//        if (!content.isEmpty()) {
//            finalContent.append(content);
//            if (!isFirstPrint) {
//                System.out.println("\n====================完整回复====================");
//                finalContent.append("\n====================完整回复====================");
//                isFirstPrint = true;
//            }
//            System.out.print(content);
//            //如果是contentFlux,在流的最开始添加 "完整回复"
//            contentFlux = Flux.just(content);
////            return Flux.create(sink -> {
////                sink.next(content);
////                sink.complete();
////            });
//        }
//        Flux<String> start = Flux.empty();
//        start.just("\n===========思考============\n");
//        // 合并两个 Flux，确保顺序正确
//        return Flux.concat(start,reasoningFlux, contentFlux);
//    }

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
                .concatMap(result -> handleGenerationResult(result,isFirstReasoning, isFirstContent).onBackpressureBuffer())
                // 4. 统一错误处理
                .onErrorResume(e -> {
                    log.error("Stream call failed", e);
                    return Flux.just("[ERROR]" + e.getMessage());
                });


    }
}
