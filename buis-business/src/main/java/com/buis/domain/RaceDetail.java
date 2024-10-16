package com.buis.domain;

import com.buis.common.annotation.Excel;
import com.buis.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class RaceDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @Excel(name = "id")
    private Long id;

    /**
     * 比赛(关联比赛表id)
     */
    @Excel(name = "比赛(关联比赛表id)")
    private Long race;

    /**
     * 比赛负责人姓名
     */
    @Excel(name = "比赛负责人姓名")
    private String name;

    /**
     * 比赛负责人手机号
     */
    @Excel(name = "比赛负责人手机号")
    private String phone;

    /**
     * 比赛负责人邮箱
     */
    @Excel(name = "比赛负责人邮箱")
    private String email;

    /**
     * 比赛说明材料
     */
    private String descUrl;

    /**
     * 比赛群二维码
     */
    private String groupUrl;

    /**
     * 计分规则
     */
    private Long rule;

    /**
     * 最大可报名人数
     */
    @Excel(name = "最大可报名人数")
    private Long capacity;

    /**
     * 队伍最小人数
     */
    private Long teamMin;

    /**
     * 队伍最大人数
     */
    private Long teamMax;

    /**
     * 老师最小人数
     */
    private Long teacherMin;

    /**
     * 老师最大人数
     */
    private Long teacherMax;

    /**
     * 推荐排名
     */
    @Excel(name = "推荐排名")
    private Long rRank;

    /**
     * 限制老师最大指导数量
     */
    @Excel(name = "限制老师最大指导数量")
    private Long tLimit;

    /**
     * 标识是否被删除，0表示未删除
     */
    private Integer isDeleted;

    /**
     * 分数权重信息
     */
    private List<Weight> weightList;


}
