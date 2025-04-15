package com.buis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum InnovationType {
    INNOVATION(1L, "创新训练"),
    ENTREPRENEURSHIP(2L, "创业训练");

    private final Long code;
    private final String value;

    public final static Map<Long, InnovationType> STATUS_MAP = new HashMap<>();

    static {
        for (InnovationType value : InnovationType.values()) {
            STATUS_MAP.put(value.getCode(), value);
        }
    }

    // 根据code获取枚举
    public static InnovationType getEnumByCode(Long code) {
        return STATUS_MAP.get(code);
    }

    // 根据code获取枚举对应的value
    public static String getValueByCode(Long code) {
        return STATUS_MAP.get(code).getValue();
    }
}
