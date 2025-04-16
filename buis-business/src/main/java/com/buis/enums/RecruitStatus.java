package com.buis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum RecruitStatus {
    YES(0L, "是"),
    NO(1L, "否");

    private final Long code;
    private final String value;

    public final static Map<Long, RecruitStatus> STATUS_MAP = new HashMap<>();

    static {
        for (RecruitStatus value : RecruitStatus.values()) {
            STATUS_MAP.put(value.getCode(), value);
        }
    }

    // 根据code获取枚举
    public static RecruitStatus getEnumByCode(Long code) {
        return STATUS_MAP.get(code);
    }

    // 根据code获取枚举对应的value
    public static String getValueByCode(Long code) {
        return STATUS_MAP.get(code).getValue();
    }

}
