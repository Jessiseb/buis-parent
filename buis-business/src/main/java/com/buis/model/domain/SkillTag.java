package com.buis.model.domain;

import com.buis.common.annotation.Excel;
import com.buis.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 技能标签主对象 skill_tag
 *
 * @author buis
 * @date 2026-03-11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillTag extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 标签名称
     */
    @Excel(name = "标签名称")
    private String tagName;

    /**
     * 分类
     */
    @Excel(name = "分类")
    private String category;

    /**
     * 标签描述
     */
    @Excel(name = "标签描述")
    private String description;

    /**
     * 是否启用
     */
    @Excel(name = "是否启用")
    private Integer isActive;

    /**
     * 标识是否被删除，0表示未删除
     */
    @ApiModelProperty("标识是否被删除，0表示未删除")
    private Integer isDeleted;


}
