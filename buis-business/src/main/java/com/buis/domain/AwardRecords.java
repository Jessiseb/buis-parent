package com.buis.domain;

import com.buis.common.annotation.Excel;
import com.buis.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 奖状汇总对象 award_records
 *
 * @author zjx
 * @date 2025-03-17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AwardRecords extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private Long id;

    /**
     * 获奖成员
     */
    @Excel(name = "获奖成员")
    private String members;

    /**
     * 比赛项目名称
     */
    @Excel(name = "比赛项目名称")
    private String project;

    /**
     * 比赛全称
     */
    @Excel(name = "比赛全称")
    private String competitionName;

    /**
     * 指导老师
     */
    @Excel(name = "指导老师")
    private String teacher;

    /**
     * 获奖日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "获奖日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date awardDate;

    /**
     * 获奖级别
     */
    @Excel(name = "获奖级别")
    private String awardLevel;

    /**
     * 所属院校
     */
    @Excel(name = "所属院校")
    private String school;

    /**
     * 状态(0-待审核 1-审核通过 2-审核未通过)
     */
    @Excel(name = "状态")
    private int status;

    /**
     * 是否删除
     */
    private Integer isDeleted;


}
