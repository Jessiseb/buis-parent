package com.buis.vo;

import com.buis.common.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

@Data
public class LoginVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private Long id;

    /**
     * 学号或工号
     */
    private String number;


    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;


}
