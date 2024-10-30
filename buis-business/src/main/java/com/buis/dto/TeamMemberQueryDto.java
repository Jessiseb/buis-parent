package com.buis.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TeamMemberQueryDto {

    /**
     * id
     */
    @ApiModelProperty(value = "队员的唯一标识符")
    private Long id;

    /**
     * 队伍
     */
    @ApiModelProperty(value = "所属队伍的ID")
    private Long team;
}
