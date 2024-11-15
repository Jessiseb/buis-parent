package com.buis.vo;

import com.buis.domain.RaceLanqiaoStudent;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RaceLanqiaoStudentVo extends RaceLanqiaoStudent {

    /**
     * 赛道子类别
     */
    @ApiModelProperty(value = "赛道子类别")
    private String sonTname;

    /**
     * 赛道父类别
     */
    @ApiModelProperty(value = "赛道父类别")
    private String parentTname;



}
