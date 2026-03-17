package com.buis.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RaceNumsVo {
    @ApiModelProperty("比赛名称")
    private String raceName;

    @ApiModelProperty("报名人数")
    private Integer signCount;

}
