package com.buis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum StuClass {

    CLASS_ONE(0L, "1班"),
    CLASS_TWO(1L, "2班"),
    CLASS_THREE(2L, "3班"),
    CLASS_FOUR(3L, "4班"),
    CLASS_FIVE(4L, "5班"),
    CLASS_SIX(5L, "6班"),
    CLASS_SEVEN(6L, "7班"),
    CLASS_EIGHT(7L, "8班"),
    CLASS_NINE(8L, "9班"),
    CLASS_TEN(9L, "10班");

    private final Long code;
    private final String value;

    public final static Map<Long, StuClass> STATUS_MAP = new HashMap<>();

    static {
        for (StuClass value : StuClass.values()) {
            STATUS_MAP.put(value.getCode(), value);
        }
    }

    // 根据code获取枚举
    public static StuClass getEnumByCode(Long code) {
        return STATUS_MAP.get(code);
    }

    // 根据code获取枚举对应的value
    public static String getValueByCode(Long code) {
        return STATUS_MAP.get(code).getValue();
    }

}


