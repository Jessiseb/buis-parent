package com.buis.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Data
@ApiModel("技能需求统计")
public class SkillDemandStatVo {

    @ApiModelProperty("技能ID")
    private Long skillId;

    @ApiModelProperty("技能名称")
    private String skillName;

    @ApiModelProperty("技能分类")
    private String category;

    @ApiModelProperty("需求团队数量")
    private Integer demandTeamCount;
}