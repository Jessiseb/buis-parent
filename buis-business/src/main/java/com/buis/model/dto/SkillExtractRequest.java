package com.buis.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 技能提取请求DTO
 *
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("技能提取请求")
public class SkillExtractRequest {

    /**
     * 待提取的文本
     */
    @ApiModelProperty(value = "待提取的文本", required = true)
    private String text;

    /**
     * 文本类型: user-用户描述, project-项目描述
     */
    @ApiModelProperty(value = "文本类型: user-用户描述, project-项目描述", required = true)
    private String type;

    /**
     * 已存在的技能标签列表（用于优化匹配）
     */
    @ApiModelProperty("已存在的技能标签列表")
    private List<String> existingSkills;
}