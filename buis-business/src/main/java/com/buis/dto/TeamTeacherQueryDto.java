package com.buis.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TeamTeacherQueryDto {

    /**
     * id
     */
    @ApiModelProperty(value = "队伍老师的唯一标识符")
    private Long id;

    /**
     * 比赛
     */
    @ApiModelProperty(value = "所属比赛的ID")
    private Long race;

    /**
     * 队伍
     */
    @ApiModelProperty(value = "所属队伍的ID")
    private Long team;
}
