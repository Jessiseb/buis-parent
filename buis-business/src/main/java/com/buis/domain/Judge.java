package com.buis.domain;

import com.buis.common.annotation.Excel;
import com.buis.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评委管理对象 t_judge
 *
 * @author buis
 * @date 2024-10-10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Judge extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @Excel(name = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 老师
     */
    @Excel(name = "老师")
    @ApiModelProperty(value = "老师id")
    private Long teacher;

    /**
     * 比赛
     */
    @Excel(name = "比赛")
    @ApiModelProperty(value = "比赛id")
    private Long race;

    /**
     * 标识是否被删除，0表示未删除
     */
    @ApiModelProperty(value = "标识是否被删除，0表示未删除")
    private Integer isDeleted;


}
