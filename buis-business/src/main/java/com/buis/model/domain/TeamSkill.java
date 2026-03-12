package com.buis.model.domain;

import com.buis.common.annotation.Excel;
import com.buis.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 团队技能需求关联对象 team_skill
 *
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("团队技能需求关联对象")
public class TeamSkill extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @ApiModelProperty("主键ID")
    @Excel(name = "主键ID")
    private Long id;

    /** 团队/项目ID */
    @ApiModelProperty("团队/项目ID")
    @Excel(name = "团队/项目ID")
    private Long teamId;

    /** 技能标签ID */
    @ApiModelProperty("技能标签ID")
    @Excel(name = "技能标签ID")
    private Long skillTagId;

    /** 重要等级: 1必须,2重要,3加分 */
    @ApiModelProperty("重要等级: 1必须,2重要,3加分")
    @Excel(name = "重要等级")
    private Integer importanceLevel;

    /** 技能标签信息（关联查询用） */
    @ApiModelProperty(hidden = true)
    private SkillTag skillTag;
}