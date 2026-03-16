package com.buis.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import io.swagger.annotations.ApiModel;

import java.util.List;

/**
 * 匹配结果详情VO
 */
@Data
@ApiModel("匹配结果详情VO")
public class MatchResultVo {
    /** 用户ID */
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    /** 用户名 */
    @ApiModelProperty(value = "用户名")
    private String userName;

    /** 队伍ID */
    @ApiModelProperty(value = "队伍ID")
    private Long teamId;

    /** 队伍名称 */
    @ApiModelProperty(value = "队伍名称")
    private String teamName;

    /** 总匹配度分数 (0.0-1.0) */
    @ApiModelProperty(value = "总匹配度分数")
    private Double totalMatchScore;

    /** 技能匹配度分数 */
    @ApiModelProperty(value = "技能匹配度分数")
    private Double skillMatchScore;

    /** 专业匹配加分 */
    @ApiModelProperty(value = "专业匹配加分")
    private Double majorMatchBonus;

    /** 匹配的技能列表 */
    @ApiModelProperty(value = "匹配的技能列表")
    private List<MatchedSkillVo> matchedSkills;

    /** 队伍需要但用户缺少的技能列表 */
    @ApiModelProperty(value = "队伍需要但用户缺少的技能列表")
    private List<String> missingSkills;

    /** 用户拥有但队伍不需要的技能列表 */
    @ApiModelProperty(value = "用户拥有但队伍不需要的技能列表")
    private List<String> extraSkills;

    /**
     * 匹配的技能详情VO
     */
    @Data
    @ApiModel("匹配的技能详情VO")
    public static class MatchedSkillVo {
        /** 技能名称 */
        @ApiModelProperty(value = "技能名称")
        private String skillName;

        /** 技能重要性等级 */
        @ApiModelProperty(value = "技能重要性等级")
        private Integer importanceLevel;

        /** 技能重要性等级名称 */
        @ApiModelProperty(value = "技能重要性等级名称")
        private String importanceLevelName;

        /** 用户技能置信度 */
        @ApiModelProperty(value = "用户技能置信度")
        private Double confidence;

        /** 该技能的匹配得分 */
        @ApiModelProperty(value = "该技能的匹配得分")
        private Double skillScore;
    }
}
