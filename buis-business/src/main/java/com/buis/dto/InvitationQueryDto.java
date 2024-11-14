package com.buis.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 学生与老师互选，邀请查询参数对象
 */
@Data
@ApiModel("邀请查询参数对象")
public class InvitationQueryDto {
    /**
     * 用户ID
     */
    @ApiModelProperty("邀请人ID")
    private Long userId;

/**
 * 被邀请人ID
 */
    @ApiModelProperty("被邀请人ID")
    private Long inviteeId;

    /**
     * 比赛ID
     */
    @ApiModelProperty("比赛ID")
    private Long race;

    /**
     * 队伍ID
     */
    @ApiModelProperty("队伍ID")
    private Long team;


    /**
     * 互选状态
     */
    @ApiModelProperty("互选状态")
    private Long isChoosed;

    /**
     * 选择原因
     */
    @ApiModelProperty("选择原因")
    private String reason;
}