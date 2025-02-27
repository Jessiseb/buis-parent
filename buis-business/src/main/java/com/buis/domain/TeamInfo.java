package com.buis.domain;

import com.buis.common.annotation.Excel;
import com.buis.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 队伍信息对象 team_info
 *
 * @author buis
 * @date 2024-10-11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("队伍信息对象")
@Builder
public class TeamInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 比赛
     */
    @ApiModelProperty("比赛")
    private Long race;

    /**
     * 项目名称
     */
    @ApiModelProperty("项目名称")
    @Excel(name = "项目名称")
    private String name;

    /**
     * 项目类型
     */
    @ApiModelProperty("项目类型")
    @Excel(name = "项目类型",dictType = "innovation_type")
    private int type;

    /**
     * 项目简介
     */
    @ApiModelProperty("项目简介")
    @Excel(name = "项目简介")
    private String description;

    /**
     * 队长id(负责人id)
     */
    @ApiModelProperty("队长id(负责人id)")
    private Long leader;

    /**
     * 负责人姓名
     */
    @ApiModelProperty("负责人姓名")
    @Excel(name = "负责人姓名")
    private String leaderName;

    /**
     * 负责人联系电话
     */
    @ApiModelProperty("负责人联系电话")
    @Excel(name = "负责人联系电话")
    private String phone;

    /**
     * 专业班级
     */
    @ApiModelProperty("专业班级")
    @Excel(name = "专业班级")
    @JsonProperty("sClass")
    private String sClass;

    /**
     * 负责人所在学院
     */
    @ApiModelProperty("负责人所在学院")
    private int college;

    /**
     * 第一指导老师
     */
    @ApiModelProperty("第一指导老师")
    @Excel(name = "第一指导老师")
    private String teacher1;

    /**
     * 第一指导老师所在学院
     */
    @ApiModelProperty("第一指导老师所在学院")
    private String teacher1College;

    /**
     * 第二指导老师
     */
    @ApiModelProperty("第二指导老师")
    @Excel(name = "第二指导老师")
    private String teacher2;

    /**
     * 第二指导老师所在学院
     */
    @ApiModelProperty("第二指导老师所在学院")
    private String teacher2College;


    /**
     * 申报书材料
     */
    @ApiModelProperty("申报书材料")
    private String declarationUrl;

    /**
     * 中期检查材料
     */
    @ApiModelProperty("中期检查材料")
    private String midUrl;

    /**
     * 结项材料
     */
    @ApiModelProperty("结项材料")
    private String closureUrl;

    /**
     * 奖项
     */
    @ApiModelProperty("奖项")
    @JsonProperty("rRank")
    private String rRank;

    /**
     * 材料审核状态
     */
    @ApiModelProperty("材料审核状态")
    private Long status;

    /**
     * 不合格原因
     */
    @ApiModelProperty("不合格原因")
    private String reason;

    /**
     * 标识是否被删除，0表示未删除
     */
    @ApiModelProperty("标识是否被删除，0表示未删除")
    private Integer isDeleted;


}
