package com.buis.vo;

import com.buis.bo.MemberInfoBO;
import com.buis.domain.TeamInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class TeamInfoVo extends TeamInfo {

    /**
     * 队长名
     */
    @ApiModelProperty(value = "队长的姓名")
    private String leaderName;

    /**
     * 队长手机号
     */
    @ApiModelProperty(value = "队长的联系电话")
    private String leaderPhone;

    /**
     * 队长邮箱
     */
    @ApiModelProperty(value = "队长的邮箱地址")
    private String leaderEmail;

    /**
     * 队长简介
     */
    @ApiModelProperty(value = "队长的个人简介")
    private String leaderDescription;

    /**
     * 老师职称（判断队长是否为老师）
     */
    @ApiModelProperty(value = "队长的职称，判断其是否为老师")
    private Byte leaderTitle;

    /**
     * 学院ID
     */
    @ApiModelProperty(value = "队长所属学院的ID")
    private Integer college;

    /**
     * 专业ID
     */
    @ApiModelProperty(value = "队长所属专业的ID")
    private Integer major;

    /**
     * 比赛名称
     */
    @ApiModelProperty(value = "所属比赛的名称")
    private String raceName;

    /**
     * 学院名称
     */
    @ApiModelProperty(value = "学院的名称")
    private String collegeName;

    /**
     * 专业名称
     */
    @ApiModelProperty(value = "专业的名称")
    private String majorName;

    /**
     * 队伍已有人数
     */
    @ApiModelProperty(value = "队伍当前已加入的人数")
    private Long teamNumbers;

    /**
     * 队伍已有成员
     */
    @ApiModelProperty(value = "队伍的成员信息列表")
    private List<MemberInfoBO> teamMemberList;

}
