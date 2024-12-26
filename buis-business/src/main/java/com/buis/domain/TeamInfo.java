package com.buis.domain;

import com.buis.common.annotation.Excel;
import com.buis.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 队伍信息对象 team_info
 *
 * @author buis
 * @date 2024-10-11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("队伍信息对象")
@Builder
public class TeamInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 比赛
     */
    @ApiModelProperty("比赛")
    @Excel(name = "比赛")
    private Long race;

    /**
     * 项目名称
     */
    @ApiModelProperty("项目名称")
    @Excel(name = "项目名称")
    private String name;

    /**
     * 项目简介
     */
    @ApiModelProperty("项目简介")
    @Excel(name = "项目简介")
    private String description;

    /**
     * 队长
     */
    @ApiModelProperty("队长")
    @Excel(name = "队长")
    private Long leader;

    /**
     * 招募要求
     */
    @ApiModelProperty("招募要求")
    @Excel(name = "招募要求")
    @JsonProperty("tLimit")
    private String tLimit;

    /**
     * 招募人数
     */
    @ApiModelProperty("招募人数")
    @Excel(name = "招募人数")
    private Long capacity;

    /**
     * 申报书
     */
    @ApiModelProperty("申报书")
    @Excel(name = "申报书")
    private String file;


    /**
     * 招聘开始时间
     */
    @ApiModelProperty("招聘开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "招聘开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date start;

    /**
     * 招聘结束时间
     */
    @ApiModelProperty("招聘结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "招聘结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date end;

    /**
     * 奖项
     */
    @ApiModelProperty("奖项")
    @JsonProperty("rRank")
    private String rRank;

    /**
     * 材料审核状态
     */
    @ApiModelProperty("材料审核状态")
    @Excel(name = "材料审核状态")
    private Long status;

    /**
     * 不合格原因
     */
    @ApiModelProperty("不合格原因")
    @Excel(name = "不合格原因")
    private String reason;

    /**
     * 标识是否被删除，0表示未删除
     */
    @ApiModelProperty("标识是否被删除，0表示未删除")
    private Integer isDeleted;

    /**
     * 指导老师
     */
    @ApiModelProperty("指导老师")
    private Long teacher;

}
