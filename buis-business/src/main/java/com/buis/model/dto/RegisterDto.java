package com.buis.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("用户注册信息")
public class RegisterDto {

    /**
     * 密码
     */
    @ApiModelProperty(value = "用户密码")
    private String password;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "用户邮箱")
    private String email;
    /**
     * 学号或工号
     */
    @ApiModelProperty(value = "用户邮箱")
    private String number;
    /**
     * 姓名
     */
    @ApiModelProperty(value = "用户姓名")
    private String name;

    /**
     * 验证码
     */
    @ApiModelProperty(value = "注册时的验证码")
    private String code;

    /**
     * 是否是老师
     */
    @ApiModelProperty(value = "是否是老师")
    private Integer isTeacher;

}
