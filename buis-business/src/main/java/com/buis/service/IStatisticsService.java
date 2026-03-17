package com.buis.service;

import com.buis.model.vo.ClassAwardCountVo;
import com.buis.model.vo.StudentCompetitionStatus;

import java.util.List;

/**
 * 数据统计服务相关接口
 */
public interface IStatisticsService{

    /**
     * 根据老师ID获取所带学生竞赛状态
     * @param teacherId
     * @return
     */
    List<StudentCompetitionStatus> getStudentCompetitionStatus(Long teacherId);
    
    /**
     * 获取老师负责学生的班级获奖情况
     * @param teacherId 老师ID
     * @param major 专业
     * @param sClass 班级
     * @param projectType 项目类型
     * @param year 学年
     * @return 班级获奖数量列表
     */
    List<ClassAwardCountVo> getClassAwardCount(Long teacherId, Integer major, String sClass, Integer projectType, String year);
}
