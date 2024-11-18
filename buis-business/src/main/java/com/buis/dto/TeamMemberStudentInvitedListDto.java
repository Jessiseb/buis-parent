package com.buis.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TeamMemberStudentInvitedListDto {
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户的ID")
    private Long id;

    /**
     * initiated
     */
    @ApiModelProperty(value = "0我发起的 1我被邀请的")
    private Long initiated;

    /**
     * 是否取消
     */
    @ApiModelProperty(value = "已取消")
    private Long isDeleted;

    /**
     * 互选状态
     */
    @ApiModelProperty(value = "0待接受 1已接受 2已拒绝")
    private Long isChoosed;
}
