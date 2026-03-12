package com.buis.model.dto;

import com.buis.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("查询用户请求参数")
public class UserSearchRequest {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @ApiModelProperty("id")
    private Long id;

    /**
     * 学号或工号
     */
    @ApiModelProperty("学号或工号")
    private String number;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String phone;

    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    private String name;
}
