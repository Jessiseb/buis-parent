package com.buis.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 比赛级别
 */
@Getter
@AllArgsConstructor
public enum RaceLevel {
    COLLEGE_LEVEL(0L,"院级"),
    UNIVERSITY_LEVEL(1L,"校级"),
    MUNICIPAL_LEVEL(2L,"市级"),
    PROVINCIAL_LEVEL(3L,"省级"),
    NATIONAL_LEVEL(4L,"国家级");

    private final Long code;
    private final String value;

    public final static Map<Long, RaceLevel> STATUS_MAP = new HashMap<>();

    static {
        for (RaceLevel value : RaceLevel.values()) {
            STATUS_MAP.put(value.getCode(), value);
        }
    }

    // 根据code获取枚举
    public static RaceLevel getEnumByCode(Long code) {
        return STATUS_MAP.get(code);
    }

    // 根据code获取枚举对应的value
    public static String getValueByCode(Long code) {
        return STATUS_MAP.get(code).getValue();
    }
}
