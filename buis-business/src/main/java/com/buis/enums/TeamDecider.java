package com.buis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum TeamDecider {
    LEADER(0L, "队长"),
    TEACHER(1L, "老师");

    private final Long code;
    private final String value;

    public final static Map<Long, TeamDecider> STATUS_MAP = new HashMap<>();

    static {
        for (TeamDecider value : TeamDecider.values()) {
            STATUS_MAP.put(value.getCode(), value);
        }
    }

    // 根据code获取枚举
    public static TeamDecider getEnumByCode(Long code) {
        return STATUS_MAP.get(code);
    }

    // 根据code获取枚举对应的value
    public static String getValueByCode(Long code) {
        return STATUS_MAP.get(code).getValue();
    }
}
