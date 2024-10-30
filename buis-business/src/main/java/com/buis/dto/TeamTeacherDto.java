package com.buis.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 队伍老师查询结果返回封装类
 */
@Data
public class TeamTeacherDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "队伍老师的唯一标识符")
    private Long id;

    // /**
    //  * 比赛
    //  */
    // @ApiModelProperty(value = "所属比赛的ID")
    // private Long race;

    /**
     * 队伍
     */
    @ApiModelProperty(value = "所属队伍的ID")
    private Long team;

    /**
     * 老师
     */
    @ApiModelProperty(value = "老师的唯一标识符")
    private Long teacher;

}
