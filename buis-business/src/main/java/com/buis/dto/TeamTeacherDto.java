package com.buis.dto;

import lombok.Data;

import java.io.Serializable;

/**
 *  队伍老师查询结果返回封装类
 */
@Data
public class TeamTeacherDto implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private Long id;

//    /**
//     * 比赛
//     */
//    private Long race;

    /**
     * 队伍
     */
    private Long team;

    /**
     * 老师
     */
    private Long teacher;


}
