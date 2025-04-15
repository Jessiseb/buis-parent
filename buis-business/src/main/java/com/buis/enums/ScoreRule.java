package com.buis.enums;

/**
 * 计分规则
 */
public enum ScoreRule {
    AVERAGE(0L, "取平均"),
    HIGH_LOW_AVERAGE(1L, "去掉最高最低取平均"),
    WEIGHT(2L, "计算权重"),
    HIGH_LOW_WEIGHT(3L, "去掉最高最低计算权重");

    private final Long code;
    private final String info;

    public Long getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    ScoreRule(Long code, String info) {
        this.code = code;
        this.info = info;
    }
}
