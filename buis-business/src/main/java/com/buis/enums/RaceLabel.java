package com.buis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum RaceLabel {
    CONTEST(1L, "竞赛训练"),
    SPECIAL(2L, "专项训练"),
    PERFORMANCES(3L, "展演训练"),
    LECTURE(4L, "讲座培训"),
    THOUGHT(5L, "思想提升"),
    GROW(6L, "成长履历"),
    SPECIALTY(7L, "技能特长"),
    CULTURE(8L, "文化艺术"),
    SPORTS(9L, "体育活动"),
    INNOVATION(10L, "创新创业"),
    PRACTICE(11L, "实践实习"),
    VOLUNTEER(12L, "志愿公益");


    private final Long code;
    private final String value;

    public final static Map<Long, RaceLabel> STATUS_MAP = new HashMap<>();

    static {
        for (RaceLabel value : RaceLabel.values()) {
            STATUS_MAP.put(value.getCode(), value);
        }
    }

    // 根据code获取枚举
    public static RaceLabel getEnumByCode(Long code) {
        return STATUS_MAP.get(code);
    }

    // 根据code获取枚举对应的value
    public static String getValueByCode(Long code) {
        return STATUS_MAP.get(code).getValue();
    }

}
