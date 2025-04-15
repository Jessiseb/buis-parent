package com.buis.enums;

import lombok.Getter;

@Getter
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
    PRACTICE(10L, "实践实习"),
    VOLUNTEER(10L, "志愿公益");

    private final Long code;
    private final String info;

    RaceLabel(Long code, String info) {
        this.code = code;
        this.info = info;
    }

}
