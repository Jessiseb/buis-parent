package com.buis.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("队伍老师Dto")
public class TeamMemberTeacherDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 待新增队伍id
     */
    @ApiModelProperty(value = "待新增队伍id")
    private List<Long> addTeamIds;

    /**
     * 待删除队伍id
     */
    @ApiModelProperty(value = "待删除队伍id")
    private List<Long> removeTeamIds;

    /**
     * 比赛
     */
    @ApiModelProperty("比赛")
    private Long race;

    /**
     * 老师
     */
    @ApiModelProperty("老师")
    private Long teacher;

    /**
     * 互选状态
     */
    @ApiModelProperty("互选状态")
    private Long isChoosed;
}
