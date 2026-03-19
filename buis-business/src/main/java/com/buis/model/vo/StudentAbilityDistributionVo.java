package com.buis.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * 学生能力分布VO
 *
 * @author buis
 * @date 2026-03-17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("学生能力分布VO")
public class StudentAbilityDistributionVo {
    /**
     * 评分区间
     */
    @ApiModelProperty("评分区间")
    private String scoreRange;

    /**
     * 学生数量
     */
    @ApiModelProperty("学生数量")
    private int studentCount;
}
