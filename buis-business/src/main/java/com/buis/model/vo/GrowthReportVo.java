package com.buis.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("成长报告VO")
public class GrowthReportVo {
    @ApiModelProperty("用户信息")
    private UserInfoVo userInfo;

    @ApiModelProperty("统计数据")
    private ReportStatsVo stats;

    @ApiModelProperty("获奖记录")
    private List<AwardRecordVo> awards;

    @ApiModelProperty("项目记录")
    private List<ProjectRecordVo> projects;

    @ApiModelProperty("参赛记录")
    private List<GrowthEventVo> races;
}