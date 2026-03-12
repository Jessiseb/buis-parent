package com.buis.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum TeacherTitle {
    ASSISTANT(0L, "助教"),
    LECTURER(1L, "讲师"),
    ASSOCIATE_PROFESSOR(2L, "副教授"),
    PROFESSOR(3L, "教授"),
    OUTSIDE_GUIDE(4L, "校外指导");

    private final Long code;
    private final String value;

    public final static Map<Long, TeacherTitle> STATUS_MAP = new HashMap<>();

    static {
        for (TeacherTitle value : TeacherTitle.values()) {
            STATUS_MAP.put(value.getCode(), value);
        }
    }

    // 根据code获取枚举
    public static TeacherTitle getEnumByCode(Long code) {
        return STATUS_MAP.get(code);
    }

    // 根据code获取枚举对应的value
    public static String getValueByCode(Long code) {
        return STATUS_MAP.get(code).getValue();
    }
}
