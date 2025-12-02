package com.buis.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("用户登录信息")
public class UserLoginDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "用户的邮箱地址")
    private String email;

    /**
     * 学号
     */
    @ApiModelProperty(value = "用户的学号或工号")
    private String number;

    /**
     * 密码
     */
    @ApiModelProperty(value = "用户的登录密码")
    private String passWord;

    /**
     * 验证码
     */
    @ApiModelProperty(value = "登录时的验证码")
    private String code;

    /**
     * 是否为老师
     */
    @ApiModelProperty(value = "是否为老师")
    private  Integer isTeacher;

}
