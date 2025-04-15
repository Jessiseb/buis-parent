package com.buis.domain;

import com.buis.common.annotation.Excel;
import com.buis.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 学生预报名对象 pre_sign_student
 *
 * @author buis
 * @date 2024-10-11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("学生预报名对象")
public class PreSignStudent extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @Excel(name = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 学生
     */
    @Excel(name = "学生")
    @ApiModelProperty(value = "学生id")
    private Long student;

    /**
     * 比赛
     */
    @Excel(name = "比赛")
    @ApiModelProperty(value = "比赛id")
    private Long race;

    /**
     * 学号
     */
    @Excel(name = "学号")
    @ApiModelProperty(value = "学号")
    private String number;

    /**
     * 班级
     */
    @Excel(name = "班级")
    @ApiModelProperty(value = "班级")
    private String s_class;

    /**
     * 手机号
     */
    @Excel(name = "手机号")
    @ApiModelProperty(value = "手机号")
    private String phone;

    /**
     * 标识是否被删除，0表示未删除
     */
    @ApiModelProperty(value = "标识是否被删除，0表示未删除")
    private Integer isDeleted;


}
