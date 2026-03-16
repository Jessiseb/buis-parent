package com.buis.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 个人面板-排名分析VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("个人面板-排名分析VO")
public class DashboardRankingVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("获奖等级分布")
    private List<AwardLevelDistributionItem> awardLevelDistribution;

    @ApiModelProperty("比赛类型分布")
    private List<RaceCategoryCountItem> raceCategoryDistribution;

    @ApiModelProperty("年级排名")
    private RankItem gradeRank;

    @ApiModelProperty("学院排名")
    private RankItem collegeRank;

    @ApiModelProperty("综合评分")
    private BigDecimal compositeScore;

    @ApiModelProperty("综合评分百分位(超过了百分之多少的同年级学生, 0-100)")
    private BigDecimal percentile;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ApiModel("获奖等级分布项")
    public static class AwardLevelDistributionItem implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("奖项等级")
        private String awardLevel;

        @ApiModelProperty("等级名称")
        private String awardLevelName;

        @ApiModelProperty("数量")
        private Integer count;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ApiModel("比赛类型分布项")
    public static class RaceCategoryCountItem implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("比赛类型(0创新创业,1蓝桥杯,2第二课堂)")
        private Integer raceType;

        @ApiModelProperty("类型名称")
        private String raceTypeName;

        @ApiModelProperty("参与数量")
        private Integer count;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ApiModel("排名项")
    public static class RankItem implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("当前排名")
        private Integer rank;

        @ApiModelProperty("总人数")
        private Integer total;

        @ApiModelProperty("排名维度描述(如: 2022级, 计算机学院)")
        private String scope;
    }
}
