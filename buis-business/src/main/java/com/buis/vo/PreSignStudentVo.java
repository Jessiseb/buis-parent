package com.buis.vo;

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
    private Long id;

    /**
     * 学生
     */
    private Long student;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 比赛
     */
    private Long race;

    /**
     * 比赛名称
     */
    private String raceName;

    /**
     * 预报名时间
     */
    private Date preTime;

    /**
     * 标识是否被删除，0表示未删除
     */
    private Integer isDeleted;

}
