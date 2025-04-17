package com.buis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 审核状态
 */
@Getter
@AllArgsConstructor
public enum ReviewStatus {
    PREPARE(0L, "待审核"),
    PASS(1L, "审核通过"),
    REFUSE(2L, "未通过"),
    PUBLISH(3L, " 已发布");

    private final Long code;
    private final String value;

    public final static Map<Long, ReviewStatus> STATUS_MAP = new HashMap<>();

    static {
        for (ReviewStatus value : ReviewStatus.values()) {
            STATUS_MAP.put(value.getCode(), value);
        }
    }

    // 根据code获取枚举
    public static ReviewStatus getEnumByCode(Long code) {
        return STATUS_MAP.get(code);
    }

    // 根据code获取枚举对应的value
    public static String getValueByCode(Long code) {
        return STATUS_MAP.get(code).getValue();
    }
}
