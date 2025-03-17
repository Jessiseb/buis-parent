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
 * 奖状信息对象 award
 *
 * @author zjx
 * @date 2025-03-16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Award extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private Long id;

    /**
     * 比赛id项目名称(第二课堂比赛项目)
     */
    @Excel(name = "比赛id项目名称(第二课堂比赛项目)")
    private Long raceId;

    /**
     * 团队id
     */
    @Excel(name = "团队id")
    private Long teamId;

    /**
     * 学年(2024-2025)
     */
    @Excel(name = "学年(2024-2025)")
    private String year;

    /**
     * 奖项等级(一二三等奖)
     */
    @Excel(name = "奖项等级(一二三等奖)")
    private String awardLevel;

    /**
     * 指导老师(选填)
     */
    @Excel(name = "指导老师(选填)")
    private String teachers;

    /**
     * 颁奖日期(YYY-MM-DD)
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "颁奖日期(YYY-MM-DD)", width = 30, dateFormat = "yyyy-MM-dd")
    private Date awardDate;

    /**
     * 是否删除
     */
    private Integer isDeleted;


}
