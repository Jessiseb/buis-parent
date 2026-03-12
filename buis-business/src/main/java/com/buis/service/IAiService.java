package com.buis.service;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.buis.model.dto.SkillExtractRequest;
import com.buis.model.dto.SkillExtractResult;
import com.buis.reponse.StreamResponse;
import reactor.core.publisher.Flux;

public interface IAiService {

    Flux<String> streamCallWithMessage(Generation gen, Message userMsg)
            throws NoApiKeyException, ApiException, InputRequiredException;

    Flux<String> agentCallWithMessage(String query, String sessionId);

    /**
     * 从文本中提取技能标签
     * @param request 技能提取请求
     * @return 技能提取结果
     */
    SkillExtractResult extractSkillsFromText(SkillExtractRequest request);
}
