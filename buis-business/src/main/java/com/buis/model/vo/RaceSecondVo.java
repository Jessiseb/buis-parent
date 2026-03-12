package com.buis.model.vo;

import com.buis.model.domain.RaceSecond;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class RaceSecondVo extends RaceSecond {

    @ApiModelProperty(value = "比赛名称")
    private String raceName;


    @ApiModelProperty(value = "第二课堂年份")
    private String secondName;

}
