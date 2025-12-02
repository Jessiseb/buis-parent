package com.buis.model.dto;

import com.buis.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "老师指导大创立项项目")
public class TeacherExportToDaDto {
    @Excel(name = "老师名称")
    private String teacherName;
    @Excel(name = "指导大创立项项目")
    private Long projectCount;
}
