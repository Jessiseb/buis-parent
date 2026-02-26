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

/**
 * 技能标签对象 skill_tag
 *
 * @author buis
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("技能标签对象")
public class SkillTag extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty("主键ID")
    @Excel(name = "主键ID")
    private Long id;

    /**
     * 标签名称
     */
    @ApiModelProperty("标签名称")
    @Excel(name = "标签名称")
    private String tagName;

    /**
     * 分类: tech/design/business/other
     */
    @ApiModelProperty("分类: tech/design/business/other")
    @Excel(name = "分类")
    private String category;

    /**
     * 标签描述
     */
    @ApiModelProperty("标签描述")
    @Excel(name = "标签描述")
    private String description;

    /**
     * 是否启用(1启用,0禁用)
     */
    @ApiModelProperty("是否启用(1启用,0禁用)")
    @Excel(name = "是否启用")
    private Integer isActive;

    /**
     * 分类展示名称
     */
    @ApiModelProperty(hidden = true)
    private String categoryName;
}
