package com.buis.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TeamMemberStudentInvitedDto {
    /**
     * 队伍
     */
    @ApiModelProperty(value = "所属队伍的ID")
    private Long team;

    /**
     * studentId
     */
    @ApiModelProperty(value = "队员的唯一标识符")
    private Long studentId;

    /**
     * 选择原因
     */
    @ApiModelProperty(value = "选择原因")
    private String reason;

    /**
     * 0表示用户 1表示队伍
     */
    @ApiModelProperty(value = "决策者")
    private long decider;
}
