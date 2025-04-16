package com.buis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 比赛状态字典
 */
@Getter
@AllArgsConstructor
public enum RaceStatus {
    PREPARE(0L, "筹备中"),
    CHECK(1L, "审核中"),
    JOIN(2L, "报名中"),
    RUN(3L, "进行中"),
    END(4L, "已结束");


    private final Long code;
    private final String value;

    public final static Map<Long, RaceStatus> STATUS_MAP = new HashMap<>();

    static {
        for (RaceStatus value : RaceStatus.values()) {
            STATUS_MAP.put(value.getCode(), value);
        }
    }

    // 根据code获取枚举
    public static RaceStatus getEnumByCode(Long code) {
        return STATUS_MAP.get(code);
    }

    // 根据code获取枚举对应的value
    public static String getValueByCode(Long code) {
        return STATUS_MAP.get(code).getValue();
    }

}
