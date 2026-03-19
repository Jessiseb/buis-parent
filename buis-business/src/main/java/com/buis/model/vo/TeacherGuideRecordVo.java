package com.buis.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Date;

/**
 * 老师指导记录VO
 *
 * @author buis
 * @date 2026-03-17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("老师指导记录VO")
public class TeacherGuideRecordVo {
  /**
   * 项目负责学生
   */
  @ApiModelProperty("项目负责学生")
  private String leaderName;

  /**
   * 项目/赛事名称
   */
  @ApiModelProperty("项目/赛事名称")
  private String projectName;

  /**
   * 指导时间
   */
  @ApiModelProperty("指导时间")
  private Date guideTime;

  /**
   * 指导内容
   */
  @ApiModelProperty("指导内容")
  private String guideContent;
}
