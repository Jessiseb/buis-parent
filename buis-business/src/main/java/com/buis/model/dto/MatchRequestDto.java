package com.buis.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import io.swagger.annotations.ApiModel;

/**
 * 匹配请求DTO
 */
@Data
@ApiModel("匹配请求DTO")
public class MatchRequestDto {
    /** 用户ID */
    @ApiModelProperty(value = "用户ID", required = true)
    private Long userId;

    /** 队伍ID */
    @ApiModelProperty(value = "队伍ID", required = true)
    private Long teamId;

    /** 是否包含专业匹配 */
    @ApiModelProperty(value = "是否包含专业匹配", example = "true")
    private Boolean includeMajorMatch = true;

    /** 匹配结果数量限制 */
    @ApiModelProperty(value = "匹配结果数量限制", example = "10")
    private Integer limit = 10;
}
