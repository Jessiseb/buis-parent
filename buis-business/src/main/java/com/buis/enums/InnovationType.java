package com.buis.enums;

import lombok.Getter;

@Getter
public enum InnovationType {
    INNOVATION(1L, "创新训练"),
    ENTREPRENEURSHIP(2L, "创业训练");

    private final Long code;
    private final String info;

    InnovationType(Long code, String info) {
        this.code = code;
        this.info = info;
    }

}
