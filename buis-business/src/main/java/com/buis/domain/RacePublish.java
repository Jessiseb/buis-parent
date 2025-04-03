package com.buis.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.buis.common.annotation.Excel;
import com.buis.common.core.domain.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 第二课堂活动发布表对象 race_publish
 *
 * @author buis
 * @date 2025-04-04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RacePublish extends BaseEntity
{   private static final long serialVersionUID = 1L;
    /** id */
    private Long id;

    /** 承办单位 */
    @Excel(name = "承办单位")
    private String organizers;

    /** 活动名称 */
    @Excel(name = "活动名称")
    private String name;

    /** 活动介绍 */
    @Excel(name = "活动介绍")
    private String description;

    /** 活动开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "活动开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 活动结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "活动结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /** 报名开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "报名开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date joinStart;

    /** 报名结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "报名结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date joinEnd;

    /** 活动标签（字典） */
    @Excel(name = "活动标签", readConverterExp = "字=典")
    private Long raceTab;

    /** 活动类型（字典） */
    @Excel(name = "活动类型", readConverterExp = "字=典")
    private Long raceType;

    /** 参与方式 */
    @Excel(name = "参与方式")
    private String particpationWay;

    /** 最大可报名人数 */
    @Excel(name = "最大可报名人数")
    private Long capacity;

    /** 负责老师 */
    @Excel(name = "负责老师")
    private String teacher;

    /** 负责老师信息 */
    @Excel(name = "负责老师信息")
    private String teacherInfo;

    /** 活动举办学期 */
    @Excel(name = "活动举办学期")
    private String raceTerm;

    /** 学生助理 */
    @Excel(name = "学生助理")
    private String assistant;

    /** 活动状态（字典） */
    @Excel(name = "活动状态", readConverterExp = "字=典")
    private Long status;

    /** 是否删除 */
    private Long isDeleted;


}

