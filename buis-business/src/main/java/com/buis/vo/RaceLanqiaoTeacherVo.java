package com.buis.vo;

import com.buis.common.annotation.Excel;
import com.buis.domain.RaceLanqiaoTeacher;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RaceLanqiaoTeacherVo extends RaceLanqiaoTeacher {

    /**
     * 学院名称
     */
    @Excel(name = "学院名称")
    @ApiModelProperty(value = "学院名称")
    private String collegeName;

    /**
     * 专业名称
     */
    @Excel(name = "专业名称")
    @ApiModelProperty(value = "专业名称")
    private String majorName;

}
