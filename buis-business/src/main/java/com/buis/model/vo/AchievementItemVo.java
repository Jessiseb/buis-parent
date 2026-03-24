package com.buis.model.vo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 成果明细项
 */
@Data
@Builder
public class AchievementItemVo {
    /**
     * 类型：competition(竞赛), project(大创), second_class(第二课堂)
     */
    private String type;

    /**
     * 名称
     */
    private String name;

    /**
     * 时间
     */
    private Date time;

    /**
     * 级别/状态
     */
    private String levelOrStatus;

    /**
     * 相关ID
     */
    private Long relatedId;

    /**
     * 证书URL（用于下载）
     */
    private String certificateUrl;
}
