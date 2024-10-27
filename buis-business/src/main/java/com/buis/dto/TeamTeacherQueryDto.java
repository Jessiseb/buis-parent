package com.buis.dto;

import lombok.Data;

@Data
public class TeamTeacherQueryDto {
    /**
     * id
     */
    private Long id;

    /**
     * 比赛
     */
    private Long race;

    /**
     * 队伍
     */
    private Long team;
}
