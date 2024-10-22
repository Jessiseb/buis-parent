package com.buis.dto;

import com.buis.common.annotation.Excel;
import lombok.Data;

@Data
public class TeamMemberQueryDTO {
    /**
     * id
     */
    private Long id;

    /**
     * 队伍
     */
    private Long team;
}
