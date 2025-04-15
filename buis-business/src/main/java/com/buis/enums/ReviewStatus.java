package com.buis.enums;

/**
 * 审核状态
 */
public enum ReviewStatus {
    PREPARE(0L, "待审核"),
    PASS(1L, "审核通过"),
    REFUSE(2L, "未通过"),
    PUBLISH(3L, " 已发布");

    private final Long code;
    private final String info;

    ReviewStatus(Long code, String info) {
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
