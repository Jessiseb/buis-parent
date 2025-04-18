package com.buis.vo;

import com.buis.domain.RaceDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("热门比赛VO")
public class HotRaceVo {
    @ApiModelProperty(value = "比赛名称")
    private String raceName;

    @ApiModelProperty(value = "报名人数")
    private Integer registrationCount;

    /**
     * 比赛实体
     */
    @ApiModelProperty(value = "比赛详情")
    private RaceDetail raceDetail;
}
