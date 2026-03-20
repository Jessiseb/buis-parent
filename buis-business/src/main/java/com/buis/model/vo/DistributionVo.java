package com.buis.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("分布数据VO")
public class DistributionVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("竞赛获奖分布")
    private List<DistributionItem> awardDistribution;

    @ApiModelProperty("大创项目状态分布")
    private List<DistributionItem> projectStatusDistribution;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ApiModel("分布项")
    public static class DistributionItem implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("类型")
        private String type;

        @ApiModelProperty("类型名称")
        private String typeName;

        @ApiModelProperty("数量")
        private Integer count;

        @ApiModelProperty("百分比")
        private Double percentage;
    }
}