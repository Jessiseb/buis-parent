package com.buis.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 队伍查询封装类
 *
 * @author zjx
 * @date 2024-10-16
 */
@Data
public class TeamQueryDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "队伍的唯一标识符")
    private Long id;

    /**
     * idList
     */
    @ApiModelProperty(value = "队伍ID列表")
    private List<Long> idList;

    /**
     * 比赛
     */
    @ApiModelProperty(value = "所属比赛的ID")
    private Long race;

    /**
     * 项目名称
     */
    @ApiModelProperty(value = "队伍的项目名称")
    private String name;

    /**
     * 队长
     */
    @ApiModelProperty(value = "队长的唯一标识符")
    private Long leader;

}
