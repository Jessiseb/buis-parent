package com.buis.model.dto;

import com.buis.model.domain.SkillTag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 技能提取结果DTO
 *
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("技能提取结果")
public class SkillExtractResult {

    /**
     * 提取成功的技能标签列表
     */
    @ApiModelProperty("提取成功的技能标签列表")
    private List<SkillTag> matchedSkills;

    /**
     * 未能匹配的原始关键词列表
     */
    @ApiModelProperty("未能匹配的原始关键词列表")
    private List<String> unmatchedKeywords;

    /**
     * 提取状态: success-成功, partial-部分匹配, failed-失败
     */
    @ApiModelProperty("提取状态: success-成功, partial-部分匹配, failed-失败")
    private String status;

    /**
     * 状态描述
     */
    @ApiModelProperty("状态描述")
    private String message;

    /**
     * 是否使用了降级方案（关键词匹配）
     */
    @ApiModelProperty("是否使用了降级方案")
    private Boolean fallbackUsed;
}