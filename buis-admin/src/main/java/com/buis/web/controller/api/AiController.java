package com.buis.web.controller.api;

import com.alibaba.dashscope.aigc.generation.Generation;
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
import com.buis.common.core.domain.R;
import com.buis.service.impl.AiServiceImpl;
import com.coze.openapi.client.chat.CreateChatReq;
import com.coze.openapi.client.chat.model.ChatEvent;
import com.coze.openapi.client.chat.model.ChatEventType;
import com.coze.openapi.service.auth.TokenAuth;
import com.coze.openapi.service.config.Consts;
import com.coze.openapi.service.service.CozeAPI;
import io.reactivex.Flowable;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import java.util.Collections;

import com.coze.openapi.client.chat.CreateChatReq;
import com.coze.openapi.client.chat.model.ChatEvent;
import com.coze.openapi.client.chat.model.ChatEventType;
import com.coze.openapi.service.auth.TokenAuth;
import com.coze.openapi.service.service.CozeAPI;

import io.reactivex.Flowable;

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
    String apiKey; //阿里云api

    @Value("${coze}")
    String cozeToken;
    //    @Value("${aliyun.base-url}")
//    String baseUrl;


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
                        Collections.singletonMap("text", "请帮忙分类图片的:活动名字、 获奖级别、全部获奖成员、全部指导老师、获奖时间,其余不需要,返回的信息必须要完全,返回的结果要以" +
                                "活动名字：内容；获奖级别：内容；获奖成员：内容；指导老师：内容；获奖时间：xxxx年-xx月-xx日，这种样子的,如果成员和老师是多个的话,用顿号隔开"),
                        Collections.singletonMap("stream", "true"))).build();
        MultiModalConversationParam param = MultiModalConversationParam.builder()
                // 若没有配置环境变量，请用百炼API Key将下行替换为：.apiKey("sk-xxx")
                .apiKey(apiKey)
                // 此处以qwen-vl-plus为例，可按需更换模型名称。模型列表：https://help.aliyun.com/zh/model-studio/getting-started/models
                .model("qwen-vl-plus")
                .message(userMessage)
                .build();
        MultiModalConversationResult result = conv.call(param);
        result.getOutput().getChoices().get(0).getMessage().getContent().forEach(System.out::println);
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

//    /**
//     * 智能体对话
//     */
//    @PostMapping("chatWithCoze")
//    public Flux<String> chatWithCoze(@RequestBody Message message) {
//        String token = cozeToken;
//
//        String botID = "7480476157490659379";
//        String userID = System.getenv("USER_ID");
//
//        TokenAuth authCli = new TokenAuth(token);
//
//        // Init the Coze client through the access_token.
//        CozeAPI coze =
//                new CozeAPI.Builder()
//                        .baseURL(Consts.COZE_CN_BASE_URL)
//                        .auth(authCli)
//                        .readTimeout(10000)
//                        .build();
//        ;
//
//     /*
//         * 第一步，创建聊天
//         * 调用 coze.chat（）.stream（） 方法创建聊天。create 方法是一个流
//         * chat 并返回一个 Flowable ChatEvent。开发人员应该迭代迭代器以获得
//         * chat 事件并处理它们。
//         * */
//        CreateChatReq req =
//                CreateChatReq.builder()
//                        .botID(botID)
//                        .userID(userID)
//                        .messages(Collections.singletonList(message.buildUserQuestionText("What can you do?")))
//                        .build();
//
//        Flowable<ChatEvent> resp = coze.chat().stream(req);
//        resp.blockingForEach(
//                event -> {
//                    if (ChatEventType.CONVERSATION_MESSAGE_DELTA.equals(event.getEvent())) {
//                        System.out.print(event.getMessage().getContent());
//                    }
//                    if (ChatEventType.CONVERSATION_CHAT_COMPLETED.equals(event.getEvent())) {
//                        System.out.println("Token usage:" + event.getChat().getUsage().getTokenCount());
//                    }
//                });
////        System.out.println("done");
//        coze.shutdownExecutor();
//        //流式返回结果
//        return Flux.from(resp)
//                .map(event -> {
//                    if (ChatEventType.CONVERSATION_MESSAGE_DELTA.equals(event.getEvent())) {
//                        return event.getMessage().getContent();
//                    } else {
//                        return "";
//                    }
//                });
//    }

}
