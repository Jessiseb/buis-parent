package com.buis.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("团队技能统计 VO")
public class TeamSkillStatisticsVO {

    @ApiModelProperty("团队ID")
    private Long teamId;

    @ApiModelProperty("技术方向技能数量")
    private Integer techCount = 0;

    @ApiModelProperty("设计方向技能数量")
    private Integer designCount = 0;

    @ApiModelProperty("商业方向技能数量")
    private Integer businessCount = 0;

    @ApiModelProperty("其他方向技能数量")
    private Integer otherCount = 0;

    @ApiModelProperty("团队总人数")
    private Integer totalMembers = 0;
}
