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
import java.util.List;

/**
 * 比赛详情对象 race_detail
 *
 * @author buis
 * @date 2024-10-11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("比赛详情对象")
public class RaceDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @Excel(name = "id")
    private Long id;

    /**
     * 比赛的一级类型id
     */
    @ApiModelProperty("比赛的一级类型id")
    private int parentId;

    /**
     * 比赛(关联比赛表id)
     */
    @ApiModelProperty("比赛(关联比赛表id)")
    private Long race;

    /**
     * 比赛负责人姓名
     */
    @ApiModelProperty("比赛负责人姓名")
    private String name;


    /**
     * 比赛负责人手机号
     */
    @ApiModelProperty("负责人手机号")
    private String phone;

    /**
     * 比赛负责人邮箱
     */
    @ApiModelProperty("比赛负责人手机号")
    private String email;

    /**
     * 比赛说明材料
     */
    @ApiModelProperty("比赛说明材料url")
    private String descUrl;

    /**
     * 比赛群二维码
     */
    @ApiModelProperty("比赛群二维码url")
    private String groupUrl;

    /**
     * 计分规则
     */
    @ApiModelProperty("计分规则")
    private Long rule;

    /**
     * 每个人可参与的项目数量
     */
    @ApiModelProperty("每个学生可参与项目数量")
    private Long count;

    /**
     * 最大可报名人数
     */
    @ApiModelProperty("最大可报名人数")
    @Excel(name = "最大可报名人数")
    private Long capacity;

    /**
     * 队伍最小人数
     */
    @ApiModelProperty("队伍最小人数")
    private int teamMin;

    /**
     * 队伍最大人数
     */
    @ApiModelProperty("队伍最大人数")
    private int teamMax;

    /**
     * 老师最小人数
     */
    @ApiModelProperty("老师最小人数")
    private int teacherMin;

    /**
     * 老师最大人数
     */
    @ApiModelProperty("老师最大人数")
    private int teacherMax;

    /**
     * 推荐排名
     */
    @ApiModelProperty("推荐排名")
    private int rRank;

    /**
     * 限制老师最大指导数量
     */
    @ApiModelProperty("限制老师最大指导数量")
    @JsonProperty("tLimit")
    private int tLimit;

    /**
     * 审核状态
     */
    @ApiModelProperty("审核状态")
    private int reviewStatus;

    /**
     * 标识是否被删除，0表示未删除
     */
    private Integer isDeleted;

    /**
     * 分数权重信息
     */
    @ApiModelProperty("分数权重")
    private List<Weight> weightList;

    /**
     *TODO 指导老师资历（职称、教学年限）
     */
    @Excel(name = "指导老师资历", readConverterExp = "职=称、教学年限")
    private String seniority;

    /**
     *TODO 所在部门
     */
    @Excel(name = "所在部门")
    private String department;

    /**
     * 承办单位
     */
    @Excel(name = "承办单位")
    @ApiModelProperty("承办单位")
    private String organizers;

    /**
     * TODO 来源课程
     */
    @Excel(name = "来源课程")
    private String course;

    /**
     * TODO 专业名称及具体学生人数
     */
    @Excel(name = "专业名称及具体学生人数")
    private String majorCount;

    /**
     * TODO 开展地点
     */
    @Excel(name = "开展地点")
    private String address;

    /**
     * TODO 经费预算
     */
    @Excel(name = "经费预算")
    private String money;

    /**
     * 活动介绍
     */
    @Excel(name = "活动介绍")
    @ApiModelProperty("活动介绍")
    private String description;

    /**
     * 活动开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "活动开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("活动开始时间")
    private Date startTime;

    /**
     * 活动结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "活动结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("活动结束时间")
    private Date endTime;

    /**
     * 活动报名开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "活动报名开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("活动报名开始时间")
    private Date joinStart;

    /**
     * 活动报名截止时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "活动报名截止时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("活动报名结束时间")
    private Date joinEnd;

    /**
     * 活动标签（字典）
     */
    @Excel(name = "活动标签", readConverterExp = "字=典")
    @ApiModelProperty("活动标签")
    private Long raceTab;

    /**
     * 活动类型（字典）
     */
    @Excel(name = "活动类型", readConverterExp = "字=典")
    @ApiModelProperty("活动类型")
    private String secondType;

    /**
     * 参与方式
     */
    @Excel(name = "参与方式")
    @ApiModelProperty("参与方式")
    private String particpationWay;

    /**
     * 负责老师
     */
    @Excel(name = "负责老师")
    private String teacher;

    /**
     * 负责老师信息
     */
    @Excel(name = "负责老师信息")
    @ApiModelProperty("负责老师信息")
    private String teacherInfo;

    /**
     * 活动举办学期
     */
    @Excel(name = "活动举办学期")
    @ApiModelProperty("活动那个举办学期")
    private String raceTerm;

    /**
     * 学生助理
     */
    @Excel(name = "学生助理")
    @ApiModelProperty("学生助理")
    private String assistant;

    /**
     * 活动状态
     */
    @Excel(name = "活动状态")
    @ApiModelProperty("活动状态")
    private Long status;

    /**
     * 活动名称
     */
    @Excel(name = "活动名称")
    @ApiModelProperty("活动名称")
    private String raceName;

    /**
     * 对应比赛模块
     */
    @Excel(name = "对应比赛模块")
    @ApiModelProperty("对应比赛模块")
    private String typeId;

    /**
     * 对应比赛模块
     */
    @Excel(name = "对应比赛模块")
    @ApiModelProperty("对应比赛模块")
    private String raceModule;
}
