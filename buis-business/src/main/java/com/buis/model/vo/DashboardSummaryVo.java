package com.buis.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 个人面板-汇总VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("个人面板-汇总VO")
public class DashboardSummaryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("数据概览")
    private DashboardOverviewVo overview;

    @ApiModelProperty("竞赛成长曲线")
    private List<GrowthCurveItemVo> growthCurve;

    @ApiModelProperty("排名分析")
    private DashboardRankingVo ranking;

    @ApiModelProperty("能力标签")
    private List<SkillCategoryVo> skills;
}
