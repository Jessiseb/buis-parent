package com.buis.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("学生创新活动趋势VO")
public class StudentInnovationTrendVo {
  @ApiModelProperty("月份")
  private String month;

  @ApiModelProperty("竞赛数量")
  private Integer competitionCount;

  @ApiModelProperty("大创项目数量")
  private Integer projectCount;

  @ApiModelProperty("第二课堂数量")
  private Integer secondClassCount;
}
