package com.buis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum NoticeType {
    Team(0L, "组队通知"),
    SYSTEM(1L, "系统通知"),
    RACE(2L, "比赛通知");

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
