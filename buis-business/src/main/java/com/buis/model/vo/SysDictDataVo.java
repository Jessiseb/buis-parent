package com.buis.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("字典信息VO")
public class SysDictDataVo {
    /** 字典标签 */
    @ApiModelProperty(value = "字典标签")
    private String dictLabel;

    /** 字典键值 */
    @ApiModelProperty(value = "字典键值")
    private String dictValue;

    /** 是否默认（Y是 N否） */
    @ApiModelProperty(value = "是否默认-Y=是,N=否")
    private String isDefault;
}
