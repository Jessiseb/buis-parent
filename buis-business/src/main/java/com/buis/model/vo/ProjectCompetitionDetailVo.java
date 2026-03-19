package com.buis.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 项目/竞赛管理明细表VO
 */
@Data
@ApiModel("项目/竞赛管理明细表VO")
public class ProjectCompetitionDetailVo {
  /**
   * 项目/赛事名称
   */
  @ApiModelProperty("项目/赛事名称")
  private String projectName;

  /**
   * 负责学生姓名
   */
  @ApiModelProperty("负责学生姓名")
  private String studentName;
}