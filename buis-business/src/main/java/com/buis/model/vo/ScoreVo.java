package com.buis.model.vo;

import com.buis.common.annotation.Excel;
import com.buis.model.domain.Score;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ScoreVo extends Score {
    /**
     * 比赛名称
     */
    @Excel(name = "比赛名称")
    @ApiModelProperty(value = "比赛名称")
    private String raceName;

    /**
     * 评委名称
     */
    @Excel(name = "评委")
    @ApiModelProperty(value = "评委")
    private String judgeName;

    /**
     * 队伍(项目)名称
     */
    @Excel(name = "队伍(项目)名称")
    @ApiModelProperty(value = "队伍(项目)名称")
    private String projectName;

}
