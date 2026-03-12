package com.buis.model.vo;

import com.buis.model.domain.SkillTag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 团队技能需求关联VO对象
 *
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("团队技能需求关联VO对象")
public class TeamSkillVo {

    /** 主键ID */
    @ApiModelProperty("主键ID")
    private Long id;

    /** 团队/项目ID */
    @ApiModelProperty("团队/项目ID")
    private Long teamId;

    /** 技能标签ID */
    @ApiModelProperty("技能标签ID")
    private Long skillTagId;

    /** 标签名称 */
    @ApiModelProperty("标签名称")
    private String tagName;

    /** 分类: tech/design/business/other */
    @ApiModelProperty("分类: tech/design/business/other")
    private String category;

    /** 标签描述 */
    @ApiModelProperty("标签描述")
    private String description;

    /** 重要等级: 1必须,2重要,3加分 */
    @ApiModelProperty("重要等级: 1必须,2重要,3加分")
    private Integer importanceLevel;

    /** 创建时间 */
    @ApiModelProperty("创建时间")
    private Date createTime;


}