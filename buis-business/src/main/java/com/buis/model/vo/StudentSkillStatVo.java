package com.buis.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("学生技能统计对象")
public class StudentSkillStatVo {
    
    @ApiModelProperty("技能名称")
    private String skillName;
    
    @ApiModelProperty("学生掌握人数")
    private Integer studentCount;
}