package com.buis.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentCompetitionStatus {
    @ApiModelProperty(value = "学生姓名")
    private String stuName;

    @ApiModelProperty(value = "参与竞赛名称")
    private List<String> raceName;
}
