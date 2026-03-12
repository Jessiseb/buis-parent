package com.buis.model.domain;

import com.buis.common.annotation.Excel;
import com.buis.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 关联第二课堂表对象 race_second
 *
 * @author buis
 * @date 2025-04-04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Second extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private Long id;

    /**
     * xx年第二课堂
     */
    @Excel(name = "xx年第二课堂")
    private String name;

    /**
     * 报名开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "报名开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /**
     * 报名结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "报名结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /**
     * 是否删除
     */
    @Excel(name = "是否删除")
    private Long isDeleted;


}
