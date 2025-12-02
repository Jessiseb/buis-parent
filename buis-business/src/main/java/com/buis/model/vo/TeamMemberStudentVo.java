package com.buis.model.vo;

import com.buis.model.domain.TeamMemberStudent;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 队伍成员管理对象 team_member_student
 *
 * @author buis
 * @date 2024-10-11
 */
@Data
@ApiModel("互选信息-队伍学生VO")
public class TeamMemberStudentVo extends TeamMemberStudent {
    /**
     * 学生信息
     */
    @ApiModelProperty(value = "学生信息")
    private UserInfoVo studentInfo;

    /**
     * 比赛名称
     */
    @ApiModelProperty(value = "所属比赛的名称")
    private String raceName;
    /**
     * 队伍名称
     */
    @ApiModelProperty(value = "所属队伍的名称")
    private String teamName;
    /**
     * 学生姓名查询
     */
    @ApiModelProperty(value = "学生姓名查询参数")
    private String studentName;
}
