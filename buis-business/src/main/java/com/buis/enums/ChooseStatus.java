package com.buis.enums;

/**
 * 互选状态
 */
public enum ChooseStatus {
    TOBESELECTED(0L, "待选"),
    CHOOSE(1L, "已选定"),
    REFUSE(2L, "已拒绝");

    private final Long code;
    private final String info;

    ChooseStatus(Long code, String info) {
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
