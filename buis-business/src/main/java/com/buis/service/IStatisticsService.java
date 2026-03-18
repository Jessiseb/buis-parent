package com.buis.service;

import com.buis.model.vo.AwardLevelDistributionVo;
import com.buis.model.vo.ClassAwardCountVo;
import com.buis.model.vo.ProjectCompetitionDetailVo;
import com.buis.model.vo.StudentCompetitionStatus;
import com.buis.model.vo.StudentCultivationDetailVo;
import com.buis.model.vo.StudentInnovationTrendVo;
import com.buis.model.vo.YearlyGuidanceResultVo;

import java.util.List;

/**
 * 数据统计服务相关接口
 */
public interface IStatisticsService {

  /**
   * 根据老师ID获取所带学生竞赛状态
   * 
   * @param teacherId
   * @return
   */
  List<StudentCompetitionStatus> getStudentCompetitionStatus(Long teacherId);

  /**
   * 获取老师负责学生的班级获奖情况
   * 
   * @param teacherId   老师ID
   * @param major       专业
   * @param sClass      班级
   * @param projectType 项目类型
   * @param year        学年
   * @return 班级获奖数量列表
   */
  List<ClassAwardCountVo> getClassAwardCount(Long teacherId, Integer major, String sClass, Integer projectType,
      String year);

  /**
   * 获取老师负责学生的学生培养明细表
   * 
   * @param teacherId   老师ID
   * @param major       专业
   * @param sClass      班级
   * @param projectType 项目类型
   * @param year        学年
   * @return 学生培养明细表列表
   */
  List<StudentCultivationDetailVo> getStudentCultivationDetail(Long teacherId, Integer major, String sClass,
      Integer projectType, String year);

  /**
   * 获取老师负责的项目/竞赛管理明细表
   * 
   * @param teacherId 老师ID
   * @return 项目/竞赛管理明细表列表
   */
  List<ProjectCompetitionDetailVo> getProjectCompetitionDetail(Long teacherId);

  /**
   * 获取老师的历年指导成果
   * 
   * @param teacherId 老师ID
   * @return 历年指导成果列表
   */
  List<YearlyGuidanceResultVo> getYearlyGuidanceResult(Long teacherId);

  /**
   * 获取老师负责项目/竞赛的获奖等级分布
   * 
   * @param teacherId 老师ID
   * @return 获奖等级分布列表
   */
  List<AwardLevelDistributionVo> getAwardLevelDistribution(Long teacherId);

  /**
   * 获取学生创新活动趋势
   * 
   * @param teacherId 老师ID
   * @return 学生创新活动趋势列表
   */
  List<StudentInnovationTrendVo> getStudentInnovationTrend(Long teacherId);
}
