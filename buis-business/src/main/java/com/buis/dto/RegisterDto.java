package com.buis.dto;

import lombok.Data;

@Data
public class RegisterDto {

    /**
     * 密码
     */
    private String password;


    /**
     * 邮箱
     */
    private String email;


    /**
     * 姓名
     */
    private String name;

    /**
     *  验证码
     */
    private String code;



}
