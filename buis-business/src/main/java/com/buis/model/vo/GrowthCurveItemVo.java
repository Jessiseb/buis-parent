package com.buis.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 个人面板-竞赛成长曲线项VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("个人面板-竞赛成长曲线项VO")
public class GrowthCurveItemVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("时间段标识, 格式: yyyy-MM")
    private String period;

    @ApiModelProperty("综合得分")
    private Double compositeScore;

    @ApiModelProperty("比赛基础分")
    private Integer raceScore;

    @ApiModelProperty("项目参与分")
    private Double projectScore;

    @ApiModelProperty("队伍参与分")
    private Double teamScore;

    @ApiModelProperty("角色加分")
    private Double roleScore;

    @ApiModelProperty("获奖加权分")
    private Double awardScore;
}
