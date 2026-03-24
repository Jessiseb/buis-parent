package com.buis.mapper;

import com.buis.model.vo.AwardLevelDistributionVo;
import com.buis.model.vo.ClassAwardCountVo;
import com.buis.model.vo.ProjectCompetitionDetailVo;
import com.buis.model.vo.StudentCultivationDetailVo;
import com.buis.model.vo.StudentInnovationTrendVo;
import com.buis.model.vo.YearlyGuidanceResultVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 统计相关Mapper
 */
@Mapper
public interface StatisticsMapper {

  /**
   * 查询老师负责学生的班级获奖情况
   * 
   * @param teacherId   老师ID
   * @param major       专业
   * @param sClass      班级
   * @param projectType 项目类型
   * @param year        学年
   * @return 班级获奖数量列表
   */
  List<ClassAwardCountVo> selectClassAwardCount(
    @Param("teacherId") Long teacherId,
    @Param("sClass") String sClass,
    @Param("projectType") Integer projectType,
    @Param("year") String year
  );

  /**
   * 查询老师负责学生的学生培养明细表
   * 
   * @param teacherId   老师ID
   * @param major       专业
   * @param sClass      班级
   * @param projectType 项目类型
   * @param year        学年
   * @return 学生培养明细表列表
   */
  List<StudentCultivationDetailVo> selectStudentCultivationDetail(
  @Param("teacherId") Long teacherId,
  @Param("sClass") String sClass,
  @Param("projectType") Integer projectType,
  @Param("year") String year
  );

  /**
   * 查询老师负责的项目/竞赛管理明细表
   * 
   * @param teacherId 老师ID
   * @return 项目/竞赛管理明细表列表
   */
  List<ProjectCompetitionDetailVo> selectProjectCompetitionDetail(Long teacherId);

  /**
   * 查询老师的历年指导成果
   * 
   * @param teacherId 老师ID
   * @return 历年指导成果列表
   */
  List<YearlyGuidanceResultVo> selectYearlyGuidanceResult(Long teacherId);

  /**
   * 查询老师负责项目/竞赛的获奖等级分布
   * 
   * @param teacherId 老师ID
   * @return 获奖等级分布列表
   */
  List<AwardLevelDistributionVo> selectAwardLevelDistribution(Long teacherId);

  /**
   * 查询学生创新活动趋势
   * 
   * @param teacherId 老师ID
   * @return 学生创新活动趋势列表
   */
  List<StudentInnovationTrendVo> selectStudentInnovationTrend(Long teacherId);
}