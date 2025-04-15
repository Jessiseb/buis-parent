package com.buis.enums;

import lombok.Getter;

@Getter
public enum TeamDecider {
    LEADER(0L, "队长"),
    TEACHER(1L, "老师");

    private final Long code;
    private final String info;

    TeamDecider(Long code, String info) {
        this.code = code;
        this.info = info;
    }
}
