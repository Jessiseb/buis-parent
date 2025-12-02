package com.buis.model.vo;

import com.buis.model.domain.TeamMemberTeacher;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 队伍老师管理对象 team_member_teacher
 *
 * @author buis
 * @date 2024-10-11
 */
@Data
@ApiModel("互选信息-队伍指导老师VO")
public class TeamMemberTeacherVo extends TeamMemberTeacher {
    /**
     * 老师信息
     */
    @ApiModelProperty(value = "老师信息")
    private UserInfoVo teacherInfo;

    /**
     * 队伍信息
     */
    @ApiModelProperty(value = "队伍信息")
    private TeamInfoVo teamInfo;

    /**
     * 比赛名称
     */
    @ApiModelProperty(value = "所属比赛的名称")
    private String raceName;

    /**
     * 决定者名称
     */


}
