package com.buis.dto;

import com.buis.common.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamMemberSelectDto {
    private static final long serialVersionUID = 1L;
    /**
     * id
     * 被邀请人的id
     */
    @Excel(name = "id")
    private Long InviteeId;

    /**
     * 比赛
     */
    @Excel(name = "比赛")
    private Long race;

    /**
     * 队伍
     */
    @Excel(name = "队伍")
    private Long team;





    /**
     * 选择原因
     */
    @Excel(name = "选择原因")
    private String reason;



}
