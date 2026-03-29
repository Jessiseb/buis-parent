package com.buis.service.impl;

import com.buis.mapper.PreSignStudentMapper;
import com.buis.mapper.StatisticsMapper;
import com.buis.model.bo.MemberInfoBO;
import com.buis.model.domain.PreSignStudent;
import com.buis.model.vo.AwardLevelDistributionVo;
import com.buis.model.vo.ClassAwardCountVo;
import com.buis.model.vo.PreSignStudentVo;
import com.buis.model.vo.ProjectCompetitionDetailVo;
import com.buis.model.vo.StudentCompetitionStatus;
import com.buis.model.vo.StudentCultivationDetailVo;
import com.buis.model.vo.StudentInnovationTrendVo;
import com.buis.model.vo.TeamInfoVo;
import com.buis.model.vo.YearlyGuidanceResultVo;
import com.buis.service.IStatisticsService;
import com.buis.service.ITeamMemberTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsServiceImpl implements IStatisticsService {
  @Autowired
  private ITeamMemberTeacherService teamMemberTeacherService;
  @Autowired
  private PreSignStudentMapper preSignStudentMapper;
  @Autowired
  private StatisticsMapper statisticsMapper;

  /**
   * 根据老师ID获取所带学生竞赛状态
   * 
   * @param teacherId
   * @return
   */
  @Override
  public List<StudentCompetitionStatus> getStudentCompetitionStatus(Long teacherId) {
    // 根据老师id获取所带团队
    List<TeamInfoVo> teamList = teamMemberTeacherService.selectJoinTeamByTeacherId(teacherId, null, null);
    // 遍历团队获取老师所带的学生id列表（需去重优化）
    List<Long> stuIdList = new ArrayList<>();
    for (TeamInfoVo teamInfoVo : teamList) {
      List<MemberInfoBO> stuList = teamInfoVo.getMemberInfoBOList();
      for (MemberInfoBO stu : stuList) {
        stuIdList.add(stu.getId());
      }
    }
    // 根据学生id列表获取所带学生报名的比赛
    List<StudentCompetitionStatus> studentCompetitionStatusesList = new ArrayList<>();
    for (Long stuId : stuIdList) {
      PreSignStudent preSignStudent = new PreSignStudent();
      preSignStudent.setStudent(stuId);
      List<PreSignStudentVo> preSignStudentVos = preSignStudentMapper.selectPreSignStudentList(preSignStudent);
      StudentCompetitionStatus aStu = new StudentCompetitionStatus();
      aStu.setStuName(preSignStudentVos.get(0).getStudentInfo().getName());
      ArrayList<String> raceNameList = new ArrayList<>();
      for (PreSignStudentVo preSignStudentVo : preSignStudentVos) {
        raceNameList.add(preSignStudentVo.getRaceName());
      }
      aStu.setRaceName(raceNameList);
      studentCompetitionStatusesList.add(aStu);
    }

    return studentCompetitionStatusesList;
  }

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
  @Override
  public List<ClassAwardCountVo> getClassAwardCount(Long teacherId, String sClass, Integer projectType,
      String year) {
    return statisticsMapper.selectClassAwardCount(teacherId, sClass, projectType, year);
  }

  /**
   * 获取老师负责学生的学生培养明细表
   * 
   * @param teacherId   老师ID
   * @param sClass      班级
   * @param projectType 项目类型
   * @param year        学年
   * @return 学生培养明细表列表
   */
  @Override
  public List<StudentCultivationDetailVo> getStudentCultivationDetail(Long teacherId, String sClass,
      Integer projectType, String year) {
    List<StudentCultivationDetailVo> rawData = statisticsMapper.selectStudentCultivationDetail(teacherId, sClass,
        projectType, year);

    Map<String, StudentCultivationDetailVo> studentMap = new HashMap<>();
    for (StudentCultivationDetailVo detail : rawData) {
      String key = detail.getStudentName() + detail.getMajorClass();
      if (studentMap.containsKey(key)) {
        StudentCultivationDetailVo existing = studentMap.get(key);
        if (detail.getAbilityTags() != null) {
          existing.getAbilityTags().addAll(detail.getAbilityTags());
        }
      } else {
        studentMap.put(key, detail);
      }
    }

    List<StudentCultivationDetailVo> result = new ArrayList<>(studentMap.values());

    int[] skillCounts = result.stream()
        .mapToInt(detail -> detail.getAbilityTags() != null ? detail.getAbilityTags().size() : 0)
        .toArray();

    int minSkillCount = skillCounts.length > 0 ? Arrays.stream(skillCounts).min().orElse(0) : 0;
    int maxSkillCount = skillCounts.length > 0 ? Arrays.stream(skillCounts).max().orElse(0) : 0;

    for (StudentCultivationDetailVo detail : result) {
      int skillCount = detail.getAbilityTags() != null ? detail.getAbilityTags().size() : 0;
      double totalScore = calculateScore(skillCount, minSkillCount, maxSkillCount);
      detail.setTotalScore(totalScore);
    }

    result.sort((a, b) -> Double.compare(b.getTotalScore(), a.getTotalScore()));

    for (int i = 0; i < result.size(); i++) {
      result.get(i).setSerialNumber(i + 1);
    }

    return result;
  }

  private double calculateScore(int skillCount, int minSkillCount, int maxSkillCount) {
    if (maxSkillCount == minSkillCount) {
      return 80.0;
    }

    double normalizedPosition = (double) (skillCount - minSkillCount) / (maxSkillCount - minSkillCount);

    double score;
    if (normalizedPosition < 0.1) {
      score = 60.0 + normalizedPosition * 100.0;
    } else if (normalizedPosition < 0.3) {
      score = 70.0 + (normalizedPosition - 0.1) * 50.0;
    } else if (normalizedPosition < 0.5) {
      score = 80.0 + (normalizedPosition - 0.3) * 50.0;
    } else if (normalizedPosition < 0.7) {
      score = 90.0 + (normalizedPosition - 0.5) * 50.0;
    } else if (normalizedPosition < 0.9) {
      score = 95.0 + (normalizedPosition - 0.7) * 25.0;
    } else {
      score = 98.0 + (normalizedPosition - 0.9) * 20.0;
    }

    score = Math.min(100.0, Math.max(60.0, score));
    return Math.round(score * 10.0) / 10.0;
  }

  @Override
  public List<ProjectCompetitionDetailVo> getProjectCompetitionDetail(Long teacherId) {
    return statisticsMapper.selectProjectCompetitionDetail(teacherId);
  }

  @Override
  public List<YearlyGuidanceResultVo> getYearlyGuidanceResult(Long teacherId) {
    return statisticsMapper.selectYearlyGuidanceResult(teacherId);
  }

  @Override
  public List<AwardLevelDistributionVo> getAwardLevelDistribution(Long teacherId) {
    return statisticsMapper.selectAwardLevelDistribution(teacherId);
  }

  @Override
  public List<StudentInnovationTrendVo> getStudentInnovationTrend(Long teacherId) {
    return statisticsMapper.selectStudentInnovationTrend(teacherId);
  }
}
