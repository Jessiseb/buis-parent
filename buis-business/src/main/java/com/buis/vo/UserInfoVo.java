package com.buis.vo;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "用户的唯一标识符")
    private Long id;

    /**
     * 学号或工号
     */
    @ApiModelProperty(value = "用户的学号或工号")
    private String number;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "用户的邮箱地址")
    private String email;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "用户的手机号码")
    private String phone;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "用户的姓名")
    private String name;

    /**
     * 个人简介
     */
    @ApiModelProperty(value = "用户的个人简介")
    private String description;

    /**
     * 用户性别
     */
    @ApiModelProperty(value = "用户的性别")
    private Long sex;

    /**
     * 学院
     */
    @ApiModelProperty(value = "用户所属学院的ID")
    private Long college;

    /**
     * 专业
     */
    @ApiModelProperty(value = "用户所属专业的ID")
    private Long major;

    /**
     * 老师职称
     */
    @ApiModelProperty(value = "用户的职称")
    private Long title;

    /**
     * 禁用状态
     */
    @ApiModelProperty(value = "用户的禁用状态")
    private Long isDisabled;

    /**
     * 标识是否被删除，0表示未删除
     */
    @ApiModelProperty(value = "是否被删除，0表示未删除")
    private Integer isDeleted;

    /**
     * 是否为老师
     */
    @ApiModelProperty(value = "标识用户是否为老师")
    private Boolean isTeacher;

    /**
     * 指导队伍数
     */
    @ApiModelProperty(value = "用户指导的队伍数量")
    private Long teamNum;

}
