package com.buis.vo;

import com.buis.common.annotation.Excel;
import lombok.Data;

@Data
public class TeamMemberStudentVO {
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

    /**
     * 学生
     */
    private Long student;
}
