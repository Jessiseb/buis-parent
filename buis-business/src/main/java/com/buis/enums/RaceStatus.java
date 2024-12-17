package com.buis.enums;

/**
 * 比赛状态
 */
public enum RaceStatus {
    PREPARE(0L, "筹备中"),
    CHECK(1L, "审核中"),
    JOIN(2L, "报名中"),
    RUN(3L, "进行中"),
    END(4L, "已结束");


    private final Long code;
    private final String info;

    RaceStatus(Long code, String info) {
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
