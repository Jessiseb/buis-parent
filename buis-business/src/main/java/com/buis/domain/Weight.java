package com.buis.domain;

import com.buis.common.annotation.Excel;
import com.buis.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分数权重对象 t_weight
 *
 * @author buis
 * @date 2024-10-11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Weight extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 比赛详情(关联比赛详情表id)
     */
    @Excel(name = "比赛详情(关联比赛详情表id)")
    private Long raceD;

    /**
     * 模块名称
     */
    @Excel(name = "模块名称")
    private String name;

    /**
     * 权重占比
     */
    @Excel(name = "权重占比")
    private Long weight;

    /**
     * 标识是否被删除，0表示未删除
     */
    private Integer isDeleted;


}
