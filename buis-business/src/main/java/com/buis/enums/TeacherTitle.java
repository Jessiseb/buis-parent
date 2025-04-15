package com.buis.enums;

public enum TeacherTitle {
    ASSISTANT(0L,"助教"),
    LECTURER(1L,"讲师"),
    ASSOCIATE_PROFESSOR(2L,"副教授"),
    PROFESSOR(3L,"教授"),
    OUTSIDE_GUIDE(4L,"校外指导");

    private final Long code;
    private final String info;

    TeacherTitle(Long code, String info) {
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
