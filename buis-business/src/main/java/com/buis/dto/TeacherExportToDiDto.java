package com.buis.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("导出老师指导的第二课堂项目数量")
public class TeacherExportToDiDto {
    @ApiModelProperty(value = "老师名称")
    private String teacherName;
    @ApiModelProperty(value = "指导第二课堂项目数量")
    private Long projectCount;

}
