package com.buis.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreSignStudentVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "预报名记录的唯一标识符")
    private Long id;

    /**
     * 学生
     */
    @ApiModelProperty(value = "学生的唯一标识符")
    private Long student;

    /**
     * 学生姓名
     */
    @ApiModelProperty(value = "学生的姓名")
    private String studentName;

    /**
     * 比赛
     */
    @ApiModelProperty(value = "所属比赛的ID")
    private Long race;

    /**
     * 比赛名称
     */
    @ApiModelProperty(value = "所属比赛的名称")
    private String raceName;

    /**
     * 预报名时间
     */
    @ApiModelProperty(value = "预报名的时间")
    private Date preTime;

    /**
     * 标识是否被删除，0表示未删除
     */
    @ApiModelProperty(value = "是否被删除，0表示未删除")
    private Integer isDeleted;

}
