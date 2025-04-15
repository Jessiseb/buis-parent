package com.buis.enums;

/**
 * 比赛类型
 */
public enum RaceType {
    COMPETITION(0L, "学科竞赛类比赛"),
    LANQIAO(1L, "蓝桥杯"),
    SECOND_CLASS(2L, "第二课堂"),
    CREATION(3L, "大创比赛");

    private final Long code;
    private final String info;

    RaceType(Long code, String info) {
        this.code = code;
        this.info = info;
    }

    public Long getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
