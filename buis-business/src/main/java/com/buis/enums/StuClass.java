package com.buis.enums;

import lombok.Getter;

@Getter
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
    private final String info;

    StuClass(Long code, String info) {
        this.code = code;
        this.info = info;
    }

}


