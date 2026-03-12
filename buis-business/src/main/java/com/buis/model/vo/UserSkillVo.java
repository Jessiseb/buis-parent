package com.buis.model.vo;

import com.buis.model.domain.SkillTag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户技能关联VO对象
 *
 * @author buis
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("用户技能关联VO对象")
public class UserSkillVo {

    /**
     * 主键ID
     */
    @ApiModelProperty("主键ID")
    private Long id;

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Long userId;

    /**
     * 技能标签ID
     */
    @ApiModelProperty("技能标签ID")
    private Long skillTagId;

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
     * 标签描述
     */
    @ApiModelProperty("标签描述")
    private String description;

    /**
     * 来源: manual/ai
     */
    @ApiModelProperty("来源: manual/ai")
    private String source;

    /**
     * 置信度(0-1)
     */
    @ApiModelProperty("置信度(0-1)")
    private BigDecimal confidence;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 技能标签信息
     */
    @ApiModelProperty("技能标签信息")
    private SkillTag skillTag;
}