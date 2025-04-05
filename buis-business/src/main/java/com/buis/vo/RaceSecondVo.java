package com.buis.vo;

import com.buis.domain.RaceSecond;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class RaceSecondVo extends RaceSecond {

    @ApiModelProperty(value = "比赛名称")
    private String raceName;

}
