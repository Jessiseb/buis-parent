package com.buis.mapper;

import com.buis.model.vo.ClassAwardCountVo;

import java.util.List;

/**
 * 统计相关Mapper
 */
public interface StatisticsMapper {
    
    /**
     * 查询老师负责学生的班级获奖情况
     * @param teacherId 老师ID
     * @param major 专业
     * @param sClass 班级
     * @param projectType 项目类型
     * @param year 学年
     * @return 班级获奖数量列表
     */
    List<ClassAwardCountVo> selectClassAwardCount(Long teacherId, Integer major, String sClass, Integer projectType, String year);
}