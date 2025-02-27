package com.buis.vo;

import com.buis.bo.MemberInfoBO;
import com.buis.common.annotation.Excel;
import com.buis.common.core.domain.BaseEntity;
import com.buis.domain.TeamInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("队伍信息VO")
public class TeamInfoVo extends TeamInfo {

    /**
     * 比赛
     */
    @ApiModelProperty(value = "比赛")
    @Excel(name="比赛",sort = 1)
    private String raceName;

    /**
     * 负责人所在学院
     */
    @ApiModelProperty(value = "负责人所在学院")
    @Excel(name="负责人所在学院")
    private String sCollege;

    /**
     * 第一指导老师所在学院
     */
    @ApiModelProperty(value = "第一指导老师所在学院")
    @Excel(name="第一指导老师所在学院")
    private String t1College;

    /**
     * 第二指导老师所在学院
     */
    @ApiModelProperty(value = "第二指导老师所在学院")
    @Excel(name="第二指导老师所在学院")
    private String t2College;

    /**
     * 队长信息
     */
    @ApiModelProperty(value = "队长信息")
    private UserInfoVo leaderInfo;
    /**
     * 比赛信息
     */
    @ApiModelProperty(value = "比赛信息")
    private RaceInfoVo raceInfoVo;
    /**
     * 队伍老师人数
     */
    @ApiModelProperty(value = "队伍老师人数")
    private Long teacherCount;

    /**
     * 队伍学生人数
     */
    @ApiModelProperty(value = "队伍学生人数")
    private Long studentCount;


}
