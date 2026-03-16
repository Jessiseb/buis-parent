package com.buis.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("报告统计VO")
public class ReportStatsVo {
    @ApiModelProperty("参赛次数")
    private Integer raceCount;

    @ApiModelProperty("获奖次数")
    private Integer awardCount;

    @ApiModelProperty("项目数量")
    private Integer projectCount;

    @ApiModelProperty("综合评分")
    private Double compositeScore;
}
