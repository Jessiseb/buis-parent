package com.buis.domain;

import com.buis.common.annotation.Excel;
import com.buis.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@ApiModel("用户信息对象")
public class UserInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @ApiModelProperty("id")
    @Excel(name = "id")
    private Long id;

    /**
     * 学号或工号
     */
    @ApiModelProperty("学号或工号")
    @Excel(name = "学号或工号")
    private String number;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    @Excel(name = "密码")
    private String password;

    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    @Excel(name = "邮箱")
    private String email;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    @Excel(name = "手机号")
    private String phone;

    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    @Excel(name = "姓名")
    private String name;

    /**
     * 个人简介
     */
    @ApiModelProperty("个人简介")
    @Excel(name = "个人简介")
    private String description;

    /**
     * 用户性别
     */
    @ApiModelProperty("用户性别")
    @Excel(name = "用户性别")
    private int sex;

    /**
     * 学院
     */
    @ApiModelProperty("学院")
    @Excel(name = "学院")
    private int college;

    /**
     * 年级
     */
    @ApiModelProperty("年级")
    @Excel(name = "年级")
    private int grade;

    /**
     * 班级
     */
    @ApiModelProperty("班级")
    @Excel(name = "班级")
    @JsonProperty("sClass")
    private String sClass;

    /**
     * 专业
     */
    @ApiModelProperty("专业")
    @Excel(name = "专业")
    private int major;

    /**
     * 老师职称
     */
    @ApiModelProperty("老师职称")
    @Excel(name = "老师职称")
    private int title;

    /**
     * 用户头像URL
     */
    @ApiModelProperty("用户头像URL")
    @Excel(name = "用户头像URL")
    private String avatarUrl;

    /**
     * 禁用状态
     */
    @ApiModelProperty("禁用状态")
    @Excel(name = "禁用状态")
    private int isDisabled;

    /**
     * 标识是否被删除，0表示未删除
     */
    @ApiModelProperty("标识是否被删除，0表示未删除")
    private Integer isDeleted;

    /**
     * 是否为老师标识 0 - 学生 1 - 老师
     */
    @ApiModelProperty("是否为老师标识 0 - 学生 1 - 老师")
    private Integer isTeacher;

    /**
     * 是否为学生助理标识 0-否 1-是
     */
    @ApiModelProperty("是否为学生助理标识 0-否 1-是")
    private Integer isAssistant ;
}
