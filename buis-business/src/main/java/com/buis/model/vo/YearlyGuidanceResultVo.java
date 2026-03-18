package com.buis.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 历年指导成果VO
 */
@Data
@ApiModel("历年指导成果VO")
public class YearlyGuidanceResultVo {
  /**
   * 年份
   */
  @ApiModelProperty("年份")
  private String year;

  /**
   * 获奖数
   */
  @ApiModelProperty("获奖数")
  private Integer awardCount;

  /**
   * 立项数量
   */
  @ApiModelProperty("立项数量")
  private Integer projectCount;
}