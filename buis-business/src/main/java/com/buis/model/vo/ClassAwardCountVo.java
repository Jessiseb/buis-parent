package com.buis.model.vo;

/**
 * 班级获奖数量统计VO
 */
public class ClassAwardCountVo {
    /**
     * 班级名称
     */
    private String className;
    
    /**
     * 获奖数量
     */
    private Integer awardCount;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getAwardCount() {
        return awardCount;
    }

    public void setAwardCount(Integer awardCount) {
        this.awardCount = awardCount;
    }
}