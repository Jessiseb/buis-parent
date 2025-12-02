package com.buis.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum RaceLabel {
    CONTEST(7L, "竞赛训练"),
    SPECIAL(8L, "专项训练"),
    TRAINING(9L, "创新训练"),
    ENTREPRENEURSHIP(11L, "创业实践"),
    SPECIALIZED(12L, "专业活动"),
    THOUGHT(13L, "思想提升"),
    CULTURE(14L, "文化艺术"),
    SPORTS(15L, "体育活动"),
    INNOVATION(16L, "创新创业"),
    PRACTICE(17L, "实践实习");


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
