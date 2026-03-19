package com.buis.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import io.swagger.annotations.ApiModel;

import java.util.List;

/**
 * 匹配过滤条件DTO
 */
@Data
@ApiModel("匹配过滤条件DTO")
public class MatchFilterDto {
    /** 最小匹配度分数 */
    @ApiModelProperty(value = "最小匹配度分数", example = "0.0")
    private Double minMatchScore = 0.0;

    /** 技能分类过滤 */
    @ApiModelProperty(value = "技能分类过滤")
    private List<String> skillCategories;

    /** 队伍类型过滤 */
    @ApiModelProperty(value = "队伍类型过滤")
    private List<Integer> teamTypes;

    /** 学院过滤 */
    @ApiModelProperty(value = "学院过滤")
    private List<Integer> colleges;

    /** 是否只显示招募中的队伍 */
    @ApiModelProperty(value = "是否只显示招募中的队伍", example = "true")
    private Boolean onlyRecruiting = true;

    /** 结果数量限制 */
    @ApiModelProperty(value = "结果数量限制", example = "10")
    private Integer limit = 10;

    /** 排序字段 */
    @ApiModelProperty(value = "排序字段", example = "matchScore")
    private String sortField = "matchScore";

    /** 排序方向（asc/desc） */
    @ApiModelProperty(value = "排序方向", example = "desc")
    private String sortDirection = "desc";
}
