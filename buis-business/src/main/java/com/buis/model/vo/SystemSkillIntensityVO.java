package com.buis.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 系统技能强度统计VO
 *
 * @author 开发者
 */
@Data
@ApiModel("所有团队技能强度统计VO")
public class SystemSkillIntensityVO {

    @ApiModelProperty("技术方向强度（技术技能总数/总人数）")
    private BigDecimal techIntensity = BigDecimal.ZERO;

    @ApiModelProperty("设计方向强度（设计技能总数/总人数）")
    private BigDecimal designIntensity = BigDecimal.ZERO;

    @ApiModelProperty("商业方向强度（商业技能总数/总人数）")
    private BigDecimal businessIntensity = BigDecimal.ZERO;

    @ApiModelProperty("其他方向强度（其他技能总数/总人数）")
    private BigDecimal otherIntensity = BigDecimal.ZERO;

    @ApiModelProperty("系统总人数（所有团队人数之和）")
    private Integer totalSystemMembers = 0;

    @ApiModelProperty("系统总技能数（所有方向技能数之和）")
    private Integer totalSystemSkillCount = 0;
}