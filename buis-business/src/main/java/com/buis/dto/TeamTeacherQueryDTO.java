package com.buis.dto;

import com.buis.common.annotation.Excel;
import lombok.Data;

@Data
public class TeamTeacherQueryDTO {
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
