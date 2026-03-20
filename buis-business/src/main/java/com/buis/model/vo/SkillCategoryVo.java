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
 * 个人面板-技能分类VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("个人面板-技能分类VO")
public class SkillCategoryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("分类标识: tech/design/business/other")
    private String category;

    @ApiModelProperty("分类名称: 技术/设计/商业/其他")
    private String categoryName;

    @ApiModelProperty("该分类下的技能列表")
    private List<SkillItem> skills;

    @ApiModelProperty("该分类下的技能数量")
    private Integer skillCount;

    @ApiModelProperty("该分类的技能总分")
    private Double categoryScore;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ApiModel("技能项")
    public static class SkillItem implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("技能标签ID")
        private Long skillTagId;

        @ApiModelProperty("技能名称")
        private String tagName;

        @ApiModelProperty("来源: manual/ai")
        private String source;

        @ApiModelProperty("置信度(0-1)")
        private BigDecimal confidence;
    }
}
