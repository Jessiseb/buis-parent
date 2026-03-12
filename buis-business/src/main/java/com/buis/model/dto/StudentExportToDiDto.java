package com.buis.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("导出学生参与的第二课堂比赛数量")
public class StudentExportToDiDto {
    @ApiModelProperty(value = "学生姓名")
    private String studentName;
    @ApiModelProperty(value = "第二课堂数量")
    private Long projectCount;

}
