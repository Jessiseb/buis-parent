package com.buis.dto;

import lombok.Data;

@Data
public class TeamMemberQueryDto {
    /**
     * id
     */
    private Long id;

    /**
     * 队伍
     */
    private Long team;
}
