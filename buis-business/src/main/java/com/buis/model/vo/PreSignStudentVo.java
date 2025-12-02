package com.buis.model.vo;

import com.buis.model.domain.PreSignStudent;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("学生预报名信息VO")
public class PreSignStudentVo extends PreSignStudent {

    /**
     * 学生信息
     */
    @ApiModelProperty(value = "学生信息")
    private UserInfoVo studentInfo;


    /**
     * 比赛名称
     */
    @ApiModelProperty(value = "所属比赛的名称")
    private String raceName;

}
