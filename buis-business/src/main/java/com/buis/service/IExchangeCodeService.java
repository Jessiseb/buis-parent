package com.buis.service;

/**
 * 项目编码服务
 */
public interface IExchangeCodeService {
    /**
     * 生成编码
     * @param teamId
     * @return
     */
    String generateCode(long teamId);
}
