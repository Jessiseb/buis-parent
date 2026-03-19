package com.buis.model.enums;
import lombok.Getter;

/**
 * 比赛子类型字典枚举（仅存储数据，无业务逻辑）
 */
@Getter
public enum RaceSonTypeEnum implements DictEnum {
    // 字典数据（编码+名称）
    COMPETITION_TRAINING(7, "竞赛训练"),
    SPECIAL_TRAINING(8, "专项训练"),
    INNOVATION_TRAINING(9, "创新训练"),
    ENTREPRENEURSHIP_TRAINING(10, "创业训练"),
    ENTREPRENEURSHIP_PRACTICE(11, "创业实践"),
    PROFESSIONAL_ACTIVITY(12, "专业活动"),
    IDEOLOGICAL_IMPROVEMENT(13, "思想提升"),
    CULTURAL_ART(14, "文化艺术"),
    SPORTS_ACTIVITY(15, "体育活动"),
    INNOVATION_ENTREPRENEURSHIP(16, "创新创业"),
    PRACTICE_INTERNSHIP(17, "实践实习"),
    VOLUNTEER_PUBLIC_WELFARE(18, "志愿公益"),
    PERFORMANCE_TRAINING(20, "展演训练"),
    LECTURE_TRAINING(25, "讲座培训"),
    GROWTH_RESUME(26, "成长履历"),
    SKILL_SPECIALTY(27, "技能特长");

    private final Integer code;  // 字典编码
    private final String name;   // 字典名称

    // 构造方法
    RaceSonTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}