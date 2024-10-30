package com.buis.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TeamMemberStudentDto {

    /**
     * 队伍
     */
    @ApiModelProperty(value = "所属队伍的ID")
    private Long team;

    /**
     * id
     */
    @ApiModelProperty(value = "队员的唯一标识符")
    private Long id;

    /**
     * 学生
     */
    @ApiModelProperty(value = "学生的唯一标识符")
    private Long student;
}
