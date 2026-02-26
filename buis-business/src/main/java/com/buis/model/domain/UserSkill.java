package com.buis.model.domain;



import com.buis.common.annotation.Excel;
import com.buis.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户技能关联对象 user_skill
 *
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("用户技能关联对象")
public class UserSkill extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty("主键ID")
    @Excel(name = "主键ID")
    private Long id;

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    @Excel(name = "用户ID")
    private Long userId;

    /**
     * 技能标签ID
     */
    @ApiModelProperty("技能标签ID")
    @Excel(name = "技能标签ID")
    private Long skillTagId;

    /**
     * 来源: manual/ai
     */
    @ApiModelProperty("来源: manual/ai")
    @Excel(name = "来源")
    private String source;

    /**
     * 置信度(0-1)
     */
    @ApiModelProperty("置信度(0-1)")
    @Excel(name = "置信度")
    private BigDecimal confidence;

    /**
     * 技能标签信息（关联查询用）
     */
    @ApiModelProperty(hidden = true)
    private SkillTag skillTag;
}