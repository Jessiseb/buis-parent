package com.buis.vo;

import com.buis.common.annotation.Excel;
import com.buis.domain.RaceDetail;
import com.buis.domain.RaceInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 比赛信息对象 race_info
 *
 * @author kaven
 * @date 2024-10-23
 */
@Data
@ApiModel("比赛信息VO")
public class RaceInfoVo extends RaceInfo {
    /**
     * 类型名称
     */
    @Excel(name = "一级类型名称")
    @ApiModelProperty(value = "一级类型名称")
    private String parentName;
    /**
     * 类型名称
     */
    @Excel(name = "二级类型名称")
    @ApiModelProperty(value = "二级类型名称")
    private String sonName;
    /**
     * 比赛实体
     */
    @ApiModelProperty(value = "比赛详情")
    private RaceDetail raceDetail;

    /**
     * 是否已报名
     */
    @ApiModelProperty(value = "是否已报名")
    private Integer isJoin;

}
