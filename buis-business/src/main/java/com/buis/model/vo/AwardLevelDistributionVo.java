package com.buis.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("获奖等级分布VO")
public class AwardLevelDistributionVo {
  @ApiModelProperty("获奖等级")
  private String awardLevel;

  @ApiModelProperty("数量")
  private Integer count;
}