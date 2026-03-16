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

    @ApiModelProperty("比赛ID")
    private Long raceId;

    @ApiModelProperty("比赛名称")
    private String raceName;

    @ApiModelProperty("比赛级别(0院级,1校级,2市级,3省级,4国家级)")
    private Long raceLevel;

    @ApiModelProperty("比赛级别名称")
    private String raceLevelName;

    @ApiModelProperty("比赛类型(0创新创业,1蓝桥杯,2第二课堂)")
    private Integer raceType;

    @ApiModelProperty("队伍/项目名称")
    private String teamName;

    @ApiModelProperty("队伍ID")
    private Long teamId;

    @ApiModelProperty("获奖等级(null表示未获奖)")
    private String awardLevel;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("比赛开始时间")
    private Date raceStart;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("比赛结束时间")
    private Date raceEnd;

    @ApiModelProperty("角色: leader/member")
    private String role;
}
