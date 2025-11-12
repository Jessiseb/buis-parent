package com.buis.model.domain;

import com.buis.common.annotation.Excel;
import com.buis.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 第二课堂活动申报表对象 race_declare
 *
 * @author buis
 * @date 2025-04-03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RaceDeclare extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private Long id;

    /**
     * 关联比赛
     */
    @Excel(name = "关联比赛")
    private String race;

    /**
     * 活动名称
     */
    @Excel(name = "活动名称")
    private String name;

    /**
     * 部门
     */
    @Excel(name = "部门")
    private String department;

    /**
     * 承办单位
     */
    @Excel(name = "承办单位")
    private String organizers;

    /**
     * 来源课程
     */
    @Excel(name = "来源课程")
    private String course;

    /**
     * 专业名及其具体学生人数
     */
    @Excel(name = "专业名及其具体学生人数")
    private String majorCount;

    /**
     * 活动类型
     */
    @Excel(name = "活动类型")
    private Long raceType;

    /**
     * 活动负责老师
     */
    @Excel(name = "活动负责老师")
    private String teacher;

    /**
     * 指导老师资历（职称、教学年限）
     */
    @Excel(name = "指导老师资历", readConverterExp = "职=称、教学年限")
    private String seniority;

    /**
     * 指导老师累计指导第二课堂活动次数
     */
    @Excel(name = "指导老师累计指导第二课堂活动次数")
    private Long count;

    /**
     * 活动开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "活动开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /**
     * 活动结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "活动结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /**
     * 开展地点
     */
    @Excel(name = "开展地点")
    private String place;

    /**
     * 项目经费
     */
    @Excel(name = "项目经费")
    private Long fund;

    /**
     * 活动审核状态(字典)
     */
    @Excel(name = "活动审核状态(字典)")
    private Long reviewStatus;

    /**
     * 是否删除
     */
    private Long isDeleted;


}
