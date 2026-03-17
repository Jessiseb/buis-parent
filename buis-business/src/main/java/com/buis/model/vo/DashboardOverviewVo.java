package com.buis.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 个人面板-数据概览VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("个人面板-数据概览VO")
public class DashboardOverviewVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("参赛总数")
    private Integer raceCount;

    @ApiModelProperty("获奖总数")
    private Integer awardCount;

    @ApiModelProperty("参加项目数")
    private Integer projectCount;

    @ApiModelProperty("组队次数")
    private Integer teamCount;
}
