package com.buis.model.domain;

import lombok.Data;

import java.util.List;

/**
 * 用户特征信息
 */
@Data
public class UserFeature {
    private Long userId;
    private List<String> skills;          // 提取的技能关键词列表
    private String major;                 // 专业
    private String className;             // 班级
    private Integer grade;                // 年级
    private String college;               // 学院
    private Long updateTime;              // 更新时间戳

    //
    public String getSkillString(){
        return skills !=null ? String.join(",",skills) : "";
    }
}
