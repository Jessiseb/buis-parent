package com.buis.service;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

public interface IAiService {

    Flux<String> streamCallWithMessage(Generation gen, Message userMsg)
            throws NoApiKeyException, ApiException, InputRequiredException;

    Flux<ServerSentEvent<String>> agentCallWithMessage(String query, String sessionId);
}
