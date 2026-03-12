package com.buis.model.domain;

import com.buis.common.annotation.Excel;
import com.buis.common.core.domain.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 通知对象 notice
 *
 * @author buis
 * @date 2025-04-20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notice extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    private Long id;

    /**
     * 标题
     */
    @Excel(name = "标题")
    private String title;

    /**
     * 内容
     */
    @Excel(name = "内容")
    private String content;

    /**
     * 通知类型（字典）
     */
    @Excel(name = "通知类型", readConverterExp = "字典")
    private Long type;

    /**
     * 是否已读（0-未读，1-已读）
     */
    @Excel(name = "是否已读", readConverterExp = "0-未读，1-已读")
    private Integer isUnread;

    /**
     *
     */
    private Integer isDeleted;

    /**
     * 接收用户id
     */
    @Excel(name = "接收用户id")
    private Long userId;

    /**
     * 队伍id（组队邀请）
     */
    @Excel(name = "队伍id")
    private Long teamId;

    /**
     * 发送者id
     */
    @Excel(name = "发送者id")
    private Long SenderId;


}
