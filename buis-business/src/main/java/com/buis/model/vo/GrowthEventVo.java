package com.buis.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("成长事件VO")
public class GrowthEventVo {

    @ApiModelProperty("事件类型：race(参赛)、award(获奖)、project(项目)、certificate(证书)")
    private String eventType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("事件时间")
    private Date eventTime;

    @ApiModelProperty("事件名称")
    private String eventName;

    @ApiModelProperty("事件描述")
    private String eventDescription;

    @ApiModelProperty("相关ID")
    private Long relatedId;

}
