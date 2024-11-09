package com.buis.vo;

import com.buis.domain.PreSignStudent;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

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
