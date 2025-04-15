package com.buis.enums;

/**
 * 比赛级别
 */
public enum RaceLevel {
    COLLEGE_LEVEL(0L,"院级"),
    UNIVERDITY_LEVEL(1L,"校级"),
    MUNICIPAL_LEVEL(2L,"市级"),
    PROVINCIAL_LEVEL(3L,"省级"),
    NATIONAL_LEVEL(4L,"国家级");

    private Long level;
    private String name;

    RaceLevel(Long level, String name) {
        this.level = level;
        this.name = name;
    }

    public Long getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }
}
