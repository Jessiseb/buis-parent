package com.buis.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 邮箱
     */
    private String email;


    /**
     *  密码
     */
    private String passWord;


    /**
     *  验证码
     */
    private String code;

}
