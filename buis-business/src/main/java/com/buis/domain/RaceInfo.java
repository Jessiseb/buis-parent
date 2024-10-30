package com.buis.domain;

import com.buis.common.annotation.Excel;
import com.buis.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

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
public class RaceInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @Excel(name = "id")
    private Long id;

    /**
     * 一级类型
     */
    @Excel(name = "一级类型")
    private Long parentTid;

    /**
     * 二级类型
     */
    @Excel(name = "二级类型")
    private Long sonTid;

    /**
     * 比赛名称
     */
    @Excel(name = "比赛名称")
    private String name;

    /**
     * 比赛简介
     */
    @Excel(name = "比赛简介")
    private String description;

    /**
     * 报名开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "报名开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date joinStart;

    /**
     * 报名结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "报名结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date joinEnd;

    /**
     * 比赛开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "比赛开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date raceStart;

    /**
     * 比赛结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "比赛结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date raceEnd;

    /**
     * 比赛状态（字典）
     */
    @Excel(name = "比赛状态", readConverterExp = "字典")
    private Long status;

    /**
     * 比赛级别（字典）
     */
    @Excel(name = "比赛级别", readConverterExp = "字典")
    private Long level;

    /**
     * 标识是否被删除，0表示未删除
     */
    private Integer isDeleted;

    /**
     * 奖项信息
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private List<Reward> rewardList;


}
