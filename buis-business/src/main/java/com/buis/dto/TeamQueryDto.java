package com.buis.dto;

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
    private Long id;

    /**
     * idList
     */
    private List<Long> idList;

    /**
     * 比赛
     */
    private Long race;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 队长
     */
    private Long leader;


}
