package com.buis.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
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
     * 姓名
     */
    @ApiModelProperty(value = "用户姓名")
    private String name;

    /**
     * 验证码
     */
    @ApiModelProperty(value = "注册时的验证码")
    private String code;

}
