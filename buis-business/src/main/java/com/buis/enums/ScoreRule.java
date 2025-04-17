package com.buis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 计分规则
 */
@Getter
@AllArgsConstructor
public enum ScoreRule {
    AVERAGE(0L, "取平均"),
    HIGH_LOW_AVERAGE(1L, "去掉最高最低取平均"),
    WEIGHT(2L, "计算权重"),
    HIGH_LOW_WEIGHT(3L, "去掉最高最低计算权重");

    private final Long code;
    private final String value;

    public final static Map<Long, ScoreRule> STATUS_MAP = new HashMap<>();

    static {
        for (ScoreRule value : ScoreRule.values()) {
            STATUS_MAP.put(value.getCode(), value);
        }
    }

    // 根据code获取枚举
    public static ScoreRule getEnumByCode(Long code) {
        return STATUS_MAP.get(code);
    }

    // 根据code获取枚举对应的value
    public static String getValueByCode(Long code) {
        return STATUS_MAP.get(code).getValue();
    }
}
