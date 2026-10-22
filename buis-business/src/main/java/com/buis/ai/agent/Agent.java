package com.buis.ai.agent;


import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;
import reactor.core.publisher.Flux;

@AiService(
        wiringMode = AiServiceWiringMode.EXPLICIT,
        streamingChatModel = "qwenStreamingChatModel"
//        chatMemoryProvider = "chatMemoryProvider",
//        contentRetriever = "contentRetrieverAIPincone"
)
public interface Agent {
    @SystemMessage(fromResource = "documents/system.txt")
    Flux<String> chat(String userMessage);
}
