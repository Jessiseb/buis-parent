package com.buis.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("队伍成员DTO")
public class TeamMemberDto {

    /**
     * 比赛
     */
    @ApiModelProperty(value = "比赛")
    private Long race;

    /**
     * 队伍
     */
    @ApiModelProperty(value = "队伍")
    private Long team;
    /**
     * 用户
     */
    @ApiModelProperty(value = "用户")
    private Long user;

    /**
     * 决策方向
     */
    @ApiModelProperty("邀请决策标识(0-用户方，1-队伍方)")
    private Long decisionRole;

    /**
     * 用户老师标识
     */
    @ApiModelProperty("用户老师标识")
    private Long isTeacher;

    /**
     * 选择状态
     */
    @ApiModelProperty("选择状态(0-待选,1-已选定,2-已拒绝)")
    private Long isChoosed;

    /**
     * 选择原因
     */
    @ApiModelProperty(value = "选择原因")
    private String reason;



}
