package com.buis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum StuGrade {

    GRADE_2024(0L, "2024级"),
    GRADE_2023(1L, "2023级"),
    GRADE_2022(2L, "2022级"),
    GRADE_2021(3L, "2021级"),
    GRADE_A2024(4L, "A2024级"),
    GRADE_A2023(5L, "A2023级"),
    GRADE_A2022(6L, "A2022级"),
    GRADE_A2021(7L, "A2021级"),
    GRADE_B2024(8L, "B2024级"),
    GRADE_B2023(9L, "B2023级");

    private final Long code;
    private final String value;

    public final static Map<Long, StuGrade> STATUS_MAP = new HashMap<>();

    static {
        for (StuGrade value : StuGrade.values()) {
            STATUS_MAP.put(value.getCode(), value);
        }
    }

    // 根据code获取枚举
    public static StuGrade getEnumByCode(Long code) {
        return STATUS_MAP.get(code);
    }

    // 根据code获取枚举对应的value
    public static String getValueByCode(Long code) {
        return STATUS_MAP.get(code).getValue();
    }

}
