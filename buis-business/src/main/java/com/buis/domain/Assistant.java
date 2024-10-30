package com.buis.domain;

import com.buis.common.annotation.Excel;
import com.buis.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 学生助理对象 t_assistant
 *
 * @author buis
 * @date 2024-10-11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Assistant extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @Excel(name = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 学生助理
     */
    @ApiModelProperty(value = "学生助理id")
    @Excel(name = "学生助理")
    private Long student;

    /**
     * 比赛
     */
    @ApiModelProperty(value = "比赛id")
    @Excel(name = "比赛")
    private Long race;

    /**
     * 标识是否被删除，0表示未删除
     */
    @ApiModelProperty(value = "标识是否被删除，0表示未删除")
    private Integer isDeleted;


}
