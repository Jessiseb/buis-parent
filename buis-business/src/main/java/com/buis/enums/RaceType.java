package com.buis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 比赛类型
 */
@Getter
@AllArgsConstructor
public enum RaceType {
    COMPETITION(0L, "学科竞赛类比赛"),
    LANQIAO(1L, "蓝桥杯"),
    SECOND_CLASS(2L, "第二课堂"),
    CREATION(3L, "大创比赛");

    private final Long code;
    private final String value;

    public final static Map<Long, RaceType> STATUS_MAP = new HashMap<>();

    static {
        for (RaceType value : RaceType.values()) {
            STATUS_MAP.put(value.getCode(), value);
        }
    }

    // 根据code获取枚举
    public static RaceType getEnumByCode(Long code) {
        return STATUS_MAP.get(code);
    }

    // 根据code获取枚举对应的value
    public static String getValueByCode(Long code) {
        return STATUS_MAP.get(code).getValue();
    }
}
