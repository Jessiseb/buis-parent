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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("赛事提醒VO")
public class RaceReminderVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("赛事ID")
    private Long raceId;

    @ApiModelProperty("赛事名称")
    private String raceName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("比赛开始时间")
    private Date raceStart;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("比赛结束时间")
    private Date raceEnd;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("报名开始时间")
    private Date joinStart;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("报名截止时间")
    private Date joinEnd;
}