package com.buis.model.domain;

import com.buis.common.annotation.Excel;
import com.buis.common.core.domain.TreeEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 比赛类型对象 race_type
 *
 * @author buis
 * @date 2024-10-23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("比赛类型对象")
@Builder
public class RaceType extends TreeEntity {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @Excel(name = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 类型名称
     */
    @Excel(name = "类型名称")
    @ApiModelProperty(value = "类型名称")
    private String name;

    @ApiModelProperty(value = "父级id")
    private Long parentId;

    /**
     * 排序号
     */
    @Excel(name = "排序号")
    @ApiModelProperty(value = "排序号")
    private Long sort;

    /**
     * 标识是否被删除，0表示未删除
     */
    @ApiModelProperty(value = "是否被删除")
    private Integer isDeleted;


}
