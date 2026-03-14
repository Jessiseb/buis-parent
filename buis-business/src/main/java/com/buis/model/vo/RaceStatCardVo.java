package com.buis.model.vo;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 竞赛统计卡片数据VO
 */
@Data
public class RaceStatCardVo implements Serializable {
    /**
     * 卡片标题
     */
    private String title;

    /**
     * 卡片数值
     */
    private String value;

    /**
     * 较上月变化
     */
    private String changePercent;

    public RaceStatCardVo() {}

    public RaceStatCardVo(String title, String value, String changePercent) {
        this.title = title;
        this.value = value;
        this.changePercent = changePercent;
    }
}