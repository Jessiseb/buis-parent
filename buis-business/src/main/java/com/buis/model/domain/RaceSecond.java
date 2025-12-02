package com.buis.model.domain;

import com.buis.common.annotation.Excel;
import com.buis.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 第二课堂和race_info中间表对象 race_second
 *
 * @author buis
 * @date 2025-04-07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RaceSecond extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private Long id;

    /**
     * 关联race_second表
     */
    @Excel(name = "关联race_second表")
    @ApiModelProperty(value = "关联race_second表")
    private Long second;

    /**
     * 关联race_info表
     */
    @Excel(name = "关联race_info表")
    @ApiModelProperty(value = "关联race_info表")
    private Long race;

    /**
     * 审核状态
     */
    @Excel(name = "审核状态")
    @ApiModelProperty(value = "审核状态")
    private Long reviewStatus;

    /**
     * 是否删除
     */
    private Long isDeleted;


}
