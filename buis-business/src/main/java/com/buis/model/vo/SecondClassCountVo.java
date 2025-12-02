package com.buis.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("第二课堂数量VO")
public class SecondClassCountVo {
    @ApiModelProperty(value = "学院")
    private String CollegeName;

    @ApiModelProperty(value = "数量")
    private int count;
}
