package com.buis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 互选状态
 */
@Getter
@AllArgsConstructor
public enum ChooseStatus {
    TOBESELECTED(0L, "待选"),
    CHOOSE(1L, "已选定"),
    REFUSE(2L, "已拒绝");

    private final Long code;
    private final String value;

    public final static Map<Long, ChooseStatus> STATUS_MAP = new HashMap<>();

    static {
        for (ChooseStatus value : ChooseStatus.values()) {
            STATUS_MAP.put(value.getCode(), value);
        }
    }

    // 根据code获取枚举
    public static ChooseStatus getEnumByCode(Long code) {
        return STATUS_MAP.get(code);
    }

    // 根据code获取枚举对应的value
    public static String getValueByCode(Long code) {
        return STATUS_MAP.get(code).getValue();
    }
}
