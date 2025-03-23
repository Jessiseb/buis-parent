package com.buis.vo;

import com.buis.common.annotation.Excel;
import com.buis.domain.Award;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 奖项信息视图
 */
@Data
@ApiModel("奖项信息VO")
public class AwardVo extends Award {

    /**
     * 比赛名称
     */
    @ApiModelProperty("比赛名称")
    @Excel(name = "比赛名称")
    private String raceName;


    /**
     * 获奖成员
     */
    @ApiModelProperty(value = "获奖成员姓名", required = true, example = "张三,李四")
    private String winners;



    /**
     * 指导老师
     */
    @ApiModelProperty(value = "指导老师姓名", example = "王老师")
    private String teachers;

    /**
     * 比赛id项目名称(第二课堂比赛项目)
     */
    @Excel(name = "比赛id项目名称(第二课堂比赛项目)")
    private Long raceId;
}
