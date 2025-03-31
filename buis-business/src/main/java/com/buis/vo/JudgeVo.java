package com.buis.vo;

import com.buis.common.annotation.Excel;
import com.buis.domain.Judge;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 评委管理对象 judge
 *
 * @author buis
 * @date 2025-3-12
 */
@Data
@ApiModel("评委VO")
public class JudgeVo extends Judge {
    /**
     * 评委信息
     */
    @ApiModelProperty(value = "评委信息")
    private UserInfoVo userInfo;

    /**
     * 比赛信息
     */
    @ApiModelProperty(value = "比赛信息")
    private RaceInfoVo raceInfo;
}
