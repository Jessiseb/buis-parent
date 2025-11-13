package com.buis.vo;

import com.buis.bo.MemberInfoBO;
import com.buis.bo.TeacherInfoBO;
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
     * 第一指导老师名称
     */
    @ApiModelProperty(value = "第一指导老师名称")
    @Excel(name="第一指导老师名称")
    private String t1Name;

    /**
     * 第二指导老师名称
     */
    @ApiModelProperty(value = "第二指导老师名称")
    @Excel(name="第二指导老师名称")
    private String t2Name;

    /**
     * 队长信息
     */
    @ApiModelProperty(value = "队长信息")
    private UserInfoVo leaderInfo;

    /**
     * 指导老师信息
     */
    @ApiModelProperty(value = "指导老师信息")
    private List<TeacherInfoBO> teacherInfoBOList;

    /**
     * 成员信息
     */
    @ApiModelProperty(value = "成员信息")
    private List<MemberInfoBO> memberInfoBOList;

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

    /**
     * 该指导老师是否已经指导该队伍
     */
    @ApiModelProperty(value = "该指导老师是否已经指导该队伍")
    private Integer isInstruct = 0;

    @ApiModelProperty(value = "互选状态")
    private Integer isChoosed = null;
}
