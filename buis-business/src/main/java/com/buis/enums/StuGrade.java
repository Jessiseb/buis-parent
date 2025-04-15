package com.buis.enums;

import lombok.Getter;

@Getter
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
    private final String info;

    StuGrade(Long code, String info) {
        this.code = code;
        this.info = info;
    }

}
