package com.buis.vo;

import com.buis.domain.PreSignTeacher;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("老师预报名信息VO")
public class PreSignTeacherVo extends PreSignTeacher {
    /**
     * 老师信息
     */
    @ApiModelProperty(value = "老师信息")
    private UserInfoVo teacherInfo;

    /**
     * 比赛名
     */
    @ApiModelProperty(value = "所属比赛的名称")
    private String raceName;



}
