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
@ApiModel("获奖记录VO")
public class AwardRecordVo {
    @ApiModelProperty("获奖ID")
    private Long awardId;

    @ApiModelProperty("奖项名称")
    private String awardName;

    @ApiModelProperty("获奖等级")
    private String awardLevel;

    @ApiModelProperty("所属比赛")
    private String raceName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("获奖时间")
    private Date awardTime;
}