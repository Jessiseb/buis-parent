package com.buis.web.controller.api;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversation;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationParam;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.MultiModalMessage;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.exception.UploadFileException;
import com.alibaba.dashscope.utils.JsonUtils;
import com.buis.common.core.domain.R;
import com.buis.service.impl.AiServiceImpl;
import io.reactivex.Flowable;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;

@Api(tags = "AI API")
@Slf4j
@RestController
@RequestMapping("/api/ai")
public class AiController {

    @Autowired
    private AiServiceImpl aiService;

    @Value("${aliyun.ai.api-key}")
    String apiKey;
    //    @Value("${aliyun.base-url}")
//    String baseUrl;
//    private final WebClient webClient;

    @GetMapping(value = "/stream-deepseek", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public Flux<String> streamDeepSeek(@RequestParam String prompt) {
////        Map<String, Object> requestBody = new HashMap<>();
////        List<Map<String, String>> messages = new ArrayList<>();
////        Map<String, String> message = new HashMap<>();
////        message.put("role", "user");
////        message.put("content", prompt);
////        messages.add(message);
////        requestBody.put("model", "deepseek-r1");
////        requestBody.put("messages",messages);
//////        requestBody.put("stream", true);
//        // 构建请求体
//        String requestBody = "{\"model\": \"deepseek-r1\",\"messages\": [{\"role\": \"user\", \"content\":" + prompt + "}]}";
//
//
//        return webClient.post()
//                .uri("/chat/completions")
//                .header("Authorization", "Bearer sk-7696a81da1cd45ce9de89843b7eb0750") // 使用注入的密钥
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(requestBody)
//                .retrieve()
//                .bodyToFlux(String.class)
//                .map(chunk -> parseChunk(chunk))
//                .onErrorResume(e -> Flux.just("Error: " + e.getMessage()));
//    }

//    private String parseChunk(String chunk) {
//        try {
//            JsonNode node = new ObjectMapper().readTree(chunk);
//            return node.path("choices").get(0).path("delta").path("content").asText();
//        } catch (Exception e) {
//            return "[Parse Error]";
//        }
//    }



    /**
     * 接入通易模型,识别图像信息
     *
     * @param image 图像路径
     */
    @PostMapping("/imageAnalysis")
    public R<MultiModalConversationResult> imageAnalysis(@RequestParam("image") String image)
            throws ApiException, NoApiKeyException, UploadFileException {
        System.out.println(apiKey);
        MultiModalConversation conv = new MultiModalConversation();
        MultiModalMessage userMessage = MultiModalMessage.builder().role(Role.USER.getValue())
                .content(Arrays.asList(
                        Collections.singletonMap("image", image),
                        Collections.singletonMap("text", "请帮忙分类附件图片的活动名字、 获奖级别、获奖成员、指导老师、获奖时间,其余不需要"),
                        Collections.singletonMap("stream", "true"))).build();
        MultiModalConversationParam param = MultiModalConversationParam.builder()
                // 若没有配置环境变量，请用百炼API Key将下行替换为：.apiKey("sk-xxx")
                .apiKey(apiKey)
                // 此处以qwen-vl-plus为例，可按需更换模型名称。模型列表：https://help.aliyun.com/zh/model-studio/getting-started/models
                .model("qwen-vl-max")
                .message(userMessage)
                .build();
        MultiModalConversationResult result = conv.call(param);
        System.out.println(JsonUtils.toJson(result));
        return R.ok(result);
    }

    /**
     * 接入deepseek,进行文本对话
     * @return
     */
    @GetMapping("/chat")
    public Flux<String> chatWithDs(@RequestParam("text") String text, HttpServletResponse response) {
        //UTF-8
        response.setCharacterEncoding("UTF-8");
        try {
            Generation gen = new Generation();
            Message userMsg = Message.builder().role(Role.USER.getValue()).content(text).build();

            Flux<String> result = aiService.streamCallWithMessage(gen, userMsg);
            return result;
        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
            log.error("ApiException: {}", e.getMessage());
        }
        System.exit(0);
        return Flux.empty();
    }

}
