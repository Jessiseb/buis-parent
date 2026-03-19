package com.buis.model.domain;

import lombok.Data;

@Data
public class UserSkillCategoryCount {
    /**
     * 技能分类（固定值：tech/design/business/other）
     */
    private String category;

    /**
     * 该分类下的技能数量
     */
    private Integer skillCount;

}
