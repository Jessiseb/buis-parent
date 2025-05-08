package com.buis.vo;

import com.buis.domain.RaceDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("热门比赛VO")
public class HotRaceVo {
    @ApiModelProperty(value = "标签名称")
    private String labelName;

    @ApiModelProperty(value = "报名人数")
    private Integer registrationCount;

}
