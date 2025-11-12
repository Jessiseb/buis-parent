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
 * 专业信息对象 t_college_major
 *
 * @author buis
 * @date 2024-10-10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("专业信息对象")
public class CollegeMajor extends TreeEntity {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @Excel(name = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 名称
     */
    @ApiModelProperty(value = "学院或专业名称")
    @Excel(name = "名称")
    private String name;

    /**
     * 所属学院
     */
    @ApiModelProperty(value = "所属学院id")
    @Excel(name = "所属学院")
    private Long college;

    /**
     * 排序号
     */
    @Excel(name = "排序号")
    @ApiModelProperty(value = "排序号")
    private Long sort;

    /**
     * 标识是否被删除，0表示未删除
     */
    @ApiModelProperty(value = "标识是否被删除，0表示未删除")
    private Integer isDeleted;


}
