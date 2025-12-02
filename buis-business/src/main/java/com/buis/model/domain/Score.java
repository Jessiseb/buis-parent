package com.buis.model.domain;

import com.buis.common.annotation.Excel;
import com.buis.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 评分管理对象 t_score
 *
 * @author buis
 * @date 2024-10-11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("评分管理对象")
@Builder
public class Score extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @Excel(name = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 比赛
     */
    @Excel(name = "比赛")
    @ApiModelProperty(value = "比赛id")
    private Long race;

    /**
     * 评委
     */
    @Excel(name = "评委")
    @ApiModelProperty(value = "评委")
    private Long teacher;

    /**
     * 队伍
     */
    @Excel(name = "队伍")
    @ApiModelProperty(value = "队伍")
    private Long team;

    /**
     * 分数构成
     */
    @Excel(name = "分数构成")
    @ApiModelProperty(value = "分数构成-评分项id拼接计算权重后的分数1:20,2:40,3:20")
    private String build;

    /**
     * 最终分数
     */
    @Excel(name = "最终分数")
    @ApiModelProperty(value = "最终分数")
    private BigDecimal score;

    /**
     * 标识是否被删除，0表示未删除
     */
    @ApiModelProperty(value = "是否被删除")
    private Integer isDeleted;


}
