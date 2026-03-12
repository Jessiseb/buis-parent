package com.buis.service;

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
}
