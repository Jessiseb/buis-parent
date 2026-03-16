package com.buis.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;

import java.util.List;

/**
 * 用户推荐结果VO
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("用户推荐结果VO")
public class UserRecommendationVo extends UserInfoVo {
    /** 匹配度分数 (0.0-1.0) */
    @ApiModelProperty(value = "匹配度分数")
    private Double matchScore;

    /** 技能匹配度分数 */
    @ApiModelProperty(value = "技能匹配度分数")
    private Double skillMatchScore;

    /** 专业匹配加分 */
    @ApiModelProperty(value = "专业匹配加分")
    private Double majorMatchBonus;

    /** 匹配的技能列表（用户有且队伍需要的技能） */
    @ApiModelProperty(value = "匹配的技能列表")
    private List<String> matchedSkills;

    /** 用户拥有的技能列表 */
    @ApiModelProperty(value = "用户拥有的技能列表")
    private List<String> userSkills;

    /** 是否专业匹配 */
    @ApiModelProperty(value = "是否专业匹配")
    private Boolean isMajorMatch;
}
