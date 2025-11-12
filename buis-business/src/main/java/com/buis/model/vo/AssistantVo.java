package com.buis.model.vo;

import com.buis.model.domain.Assistant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 学生助理管理对象 assistant
 *
 * @author buis
 * @date 2025-3-11
 */
@Data
@ApiModel("学生助理VO")
public class AssistantVo extends Assistant {
    /**
     * 学生助理信息
     */
    @ApiModelProperty(value = "学生信息")
    private UserInfoVo userInfo;

    /**
     * 比赛信息
     */
    @ApiModelProperty(value = "比赛信息")
    private RaceInfoVo raceInfo;
}
