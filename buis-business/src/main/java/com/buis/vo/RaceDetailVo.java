package com.buis.vo;

import com.buis.common.annotation.Excel;
import com.buis.domain.RaceDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RaceDetailVo extends RaceDetail {
    /**
     * 比赛名称
     */
    @ApiModelProperty("比赛名称")
    @Excel(name = "比赛名称")
    private String raceName;




}
