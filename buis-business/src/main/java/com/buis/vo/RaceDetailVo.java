package com.buis.vo;

import com.buis.common.annotation.Excel;
import com.buis.domain.RaceDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RaceDetailVo extends RaceDetail {
    /**
     * 比赛名称
     */
    @ApiModelProperty("比赛名称")
    @Excel(name = "项目名称")
    private String raceName;

    /**
     * 项目类别(根据race查询到子类型的名字(race_type))
     */
    @ApiModelProperty("项目类别")
    @Excel(name = "项目类别")
    private String raceType;

    /**
     * 项目模块(根据race查询到子类型的名字(race_type))
     */
    @ApiModelProperty("项目模块")
    @Excel(name = "项目模块")
    private String raceModule;

}
