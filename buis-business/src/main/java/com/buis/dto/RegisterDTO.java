package com.buis.dto;

import com.buis.common.annotation.Excel;
import lombok.Data;

@Data
public class RegisterDTO {
    /**
     * id
     */
    private Long id;

    /**
     * 学号或工号
     */
    private String number;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 姓名
     */
    private String name;

    /**
     *  验证码
     */
    private String code;

    /**
     * 头像
     */
    private String avatar;


}
