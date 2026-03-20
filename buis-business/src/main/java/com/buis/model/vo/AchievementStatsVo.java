package com.buis.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("成果统计VO")
public class AchievementStatsVo {
    @ApiModelProperty("获奖总数")
    private Integer awardCount;

    @ApiModelProperty("大创项目数")
    private Integer projectCount;

    @ApiModelProperty("证书总数")
    private Integer certificateCount;
}