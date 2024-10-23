package com.buis.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private Long id;

    /**
     * 邮箱
     */
    private String email;

//    /**
//     * 手机号
//     */
//    private String phone;

    /**
     *  密码
     */
    private String passWord;


    /**
     *  验证码
     */
    private String code;

}
