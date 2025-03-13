package com.buis.service;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.buis.reponse.StreamResponse;
import reactor.core.publisher.Flux;

public interface IAiService {

    Flux<String> streamCallWithMessage(Generation gen, Message userMsg)
            throws NoApiKeyException, ApiException, InputRequiredException;

    Flux<String> agentCallWithMessage(String query, String sessionId);
}
