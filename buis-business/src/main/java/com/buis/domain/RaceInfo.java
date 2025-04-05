package com.buis.domain;

import com.buis.common.annotation.Excel;
import com.buis.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 比赛信息对象 race_info
 *
 * @author kaven
 * @date 2024-10-23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("比赛信息对象")
public class RaceInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @Excel(name = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 一级类型
     */
    @Excel(name = "一级类型")
    @ApiModelProperty("一级类型 ")
    private Long parentTid;

    /**
     * 二级类型
     */
    @Excel(name = "二级类型")
    @ApiModelProperty("二级类型")
    private Long sonTid;

    /**
     * 负责老师名称
     */
    @Excel(name = "负责老师名称")
    @ApiModelProperty("负责老师名称")
    private String teacherName;

    /**
     * 比赛名称
     */
    @Excel(name = "比赛名称")
    @ApiModelProperty("比赛名称")
    private String name;

    /**
     * 比赛简介
     */
    @Excel(name = "比赛简介")
    @ApiModelProperty("比赛简介")
    private String description;

    /**
     * 报名开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "报名开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("报名开始时间")
    private Date joinStart;

    /**
     * 报名结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "报名结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("报名结束时间")
    private Date joinEnd;

    /**
     * 比赛开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "比赛开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("比赛开始时间")
    private Date raceStart;

    /**
     * 比赛结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "比赛结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("比赛结束时间")
    private Date raceEnd;

    /**
     * 评分开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "评分开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("评分开始时间")
    private Date scoreStart;

    /**
     * 评分结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "评分结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("评分结束时间")
    private Date scoreEnd;

    /**
     * 审核状态
     */
    @ApiModelProperty("审核状态")
    private Long reviewStatus;

    /**
     * 比赛状态（字典）
     */
    @Excel(name = "比赛状态", readConverterExp = "字=典")
    @ApiModelProperty("比赛状态")
    private Long status;

    /**
     * 比赛级别（字典）
     */
    @Excel(name = "比赛级别", readConverterExp = "字=典")
    @ApiModelProperty("比赛级别")
    private Long level;

    /**
     * 指导老师资历
     */
    @Excel(name = "指导老师资历")
    @ApiModelProperty("指导老师资历")
    private String seniority;

    /**
     * 活动说明材料
     */
    @Excel(name = "活动说明材料")
    @ApiModelProperty("活动说明材料")
    private String descUrl;

    /**
     * 所在部门
     */
    @Excel(name = "所在部门")
    @ApiModelProperty("所在部门")
    private String department;

    /**
     * 承办单位
     */
    @Excel(name = "承办单位")
    @ApiModelProperty("承办单位")
    private String organizers;

    /**
     * 来源课程
     */
    @Excel(name = "来源课程")
    @ApiModelProperty("来源课程")
    private String course;

    /**
     * 开展地点
     */
    @Excel(name = "开展地点")
    @ApiModelProperty("开展地点")
    private String place;

    /**
     * 项目经费
     */
    @Excel(name = "项目经费")
    @ApiModelProperty("项目经费")
    private String fund;


    /**
     * 专业名称及具体学生人数
     */
    @Excel(name = "专业名称及具体学生人数")
    @ApiModelProperty("专业名称及具体学生人数")
    private String majorCount;


    /**
     * 标识是否被删除，0表示未删除
     */
    private Integer isDeleted;

    /**
     * 奖项信息
     */
    @ApiModelProperty("奖项信息")
    private List<Reward> rewardList;

    @ApiModelProperty("比赛类型(0-创新创业类比赛，1-蓝桥杯，2-第二课堂)")
    private Integer raceType;


}
