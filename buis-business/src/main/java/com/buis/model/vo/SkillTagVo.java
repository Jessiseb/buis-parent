package com.buis.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 技能标签VO对象
 *
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("技能标签VO对象")
public class SkillTagVo {

    /**
     * 主键ID
     */
    @ApiModelProperty("主键ID")
    private Long id;

    /**
     * 标签名称
     */
    @ApiModelProperty("标签名称")
    private String tagName;

    /**
     * 分类: tech/design/business/other
     */
    @ApiModelProperty("分类: tech/design/business/other")
    private String category;

    /**
     * 分类名称（用于显示）
     */
    @ApiModelProperty("分类名称")
    private String categoryName;

    /**
     * 标签描述
     */
    @ApiModelProperty("标签描述")
    private String description;

    /**
     * 是否启用(1启用,0禁用)
     */
    @ApiModelProperty("是否启用(1启用,0禁用)")
    private Integer isActive;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;
}
