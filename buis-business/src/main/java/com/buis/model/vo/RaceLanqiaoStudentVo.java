package com.buis.model.vo;

import com.buis.common.annotation.Excel;
import com.buis.model.domain.RaceLanqiaoStudent;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RaceLanqiaoStudentVo extends RaceLanqiaoStudent {

    /**
     * 赛道子类别
     */
    @Excel(name = "报名科目",sort = 5)
    @ApiModelProperty(value = "赛道子类别")
    private String sonTname;

    /**
     * 赛道父类别
     */
    @ApiModelProperty(value = "赛道父类别")
    private String parentTname;



}
