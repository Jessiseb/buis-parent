package com.buis.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "用户的邮箱地址")
    private String email;

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

}
