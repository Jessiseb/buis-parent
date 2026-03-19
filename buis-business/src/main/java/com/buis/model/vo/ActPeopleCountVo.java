package com.buis.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("")
public class ActPeopleCountVo {

    @ApiModelProperty("活动名称")
    private String raceName;

    @ApiModelProperty("活动报名人数")
    private Integer peopleCount;
}
