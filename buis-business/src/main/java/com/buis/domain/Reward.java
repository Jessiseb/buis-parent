package com.buis.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.buis.common.annotation.Excel;
import com.buis.common.core.domain.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
/**
 * 奖项对象 t_reward
 *
 * @author kaven
 * @date 2024-10-23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("奖项对象")
public class Reward extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @ApiModelProperty(value = "id")
    private Long id;

    /** 比赛(关联比赛表id) */
    @Excel(name = "比赛(关联比赛表id)")
    @ApiModelProperty(value = "比赛id")
    private Long race;

    /** 等级 */
    @Excel(name = "等级")
    @ApiModelProperty(value = "等级")
    private String r_rank;

    /** 奖品名称 */
    @Excel(name = "奖品名称")
    @ApiModelProperty(value = "奖品名称")
    private String name;

    /** 数量 */
    @Excel(name = "数量")
    @ApiModelProperty(value = "数量")
    private Long quantity;

    /** 标识是否被删除，0表示未删除 */
    @ApiModelProperty(value = "是否被删除")
    private Integer isDeleted;


}
