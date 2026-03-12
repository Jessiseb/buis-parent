package com.buis.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 统计结果DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeStatistic {
    @ApiModelProperty(value = "年级")
    private String grade;

    @ApiModelProperty(value = "数量")
    private Long count;

    @ApiModelProperty(value = "占比")
    private Double percentage;
}