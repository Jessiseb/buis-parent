package com.buis.model.vo;

import lombok.Data;

/**
 * 比赛类型统计结果DTO
 */
@Data
public class RaceTypeCountVo {
    /**
     * 比赛类型（0-创新创业类，1-蓝桥杯，2-第二课堂）
     */
    private Integer raceType;
    
    /**
     * 该类型比赛的数量
     */
    private Long count;


}