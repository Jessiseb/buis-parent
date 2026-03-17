package com.buis.model.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 学生培养明细表VO
 */
@Data
@ApiModel("学生培养明细表VO")
public class StudentCultivationDetailVo {
    /**
     * 序号
     */
    @ApiModelProperty("序号")
    private Integer serialNumber;
    
    /**
     * 学生姓名
     */
    @ApiModelProperty("学生姓名")
    private String studentName;
    
    /**
     * 专业班级
     */
    @ApiModelProperty("专业班级")
    private String majorClass;
    
    /**
     * 综合得分
     */
    @ApiModelProperty("综合得分")
    private Double totalScore;
    
    /**
     * 能力标签
     */
    @ApiModelProperty("能力标签")
    private List<String> abilityTags;
}