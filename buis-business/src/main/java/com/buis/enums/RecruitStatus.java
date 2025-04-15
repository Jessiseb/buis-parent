package com.buis.enums;

import lombok.Getter;

@Getter
public enum RecruitStatus {
    YES(0L, "是"),
    NO(1L, "否");

    private final Long code;
    private final String info;

    RecruitStatus(Long code, String info) {
        this.code = code;
        this.info = info;
    }
}
