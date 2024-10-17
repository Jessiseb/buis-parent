package com.buis.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户信息对象 user_info
 *
 * @author buis
 * @date 2024-10-11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoVo implements Serializable {
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

    /**
     * 姓名
     */
    private String name;

    /**
     * 个人简介
     */
    private String description;

    /**
     * 用户性别
     */
    private Long sex;

    /**
     * 学院
     */
    private Long college;

    /**
     * 专业
     */
    private Long major;

    /**
     * 老师职称
     */
    private Long title;

    /**
     * 禁用状态
     */
    private Long isDisabled;

    /**
     * 标识是否被删除，0表示未删除
     */
    private Integer isDeleted;

    /**
     * 是否为老师
     */
    private Boolean isTeacher;

    /**
     * 指导队伍数
     */
    private Long teamNum;
}