package com.buis.web.controller.api;

import com.buis.common.core.domain.R;
import com.buis.common.core.page.MyPageVo;
import com.buis.model.domain.RaceDetail;
import com.buis.model.domain.RaceInfo;
import com.buis.model.domain.TeamMemberTeacher;
import com.buis.model.dto.GradeStatistic;
import com.buis.service.*;
import com.buis.model.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Api(tags = "统计Api")
@RestController
@RequestMapping("/api/statistics")
public class StatisticsApiController {
  @Autowired
  private IRaceInfoService raceInfoService;
  @Autowired
  private ITeamInfoService teamInfoService;
  @Autowired
  private ITeamMemberStudentService teamMemberStudentService;
  @Autowired
  private IStatisticsService statisticsService;
  @Autowired
  private ITeamSkillService teamSkillService;
  @Autowired
  private IPreSignStudentService preSignStudentService;
  @Autowired
  private ITeamMemberTeacherService teamMemberTeacherService;
  @Autowired
  private IAwardService awardService;
  @Autowired
  private IScoreService scoreService;

  /**
   * 统计各年级学生的参与活动
   */

  /**
   * 根据月份查询各学院第二课堂活动数量
   * 
   * @param month 没有即查询本月
   */
  @ApiOperation("查询本月各学院第二课堂活动数量")
  @GetMapping("/getSecondClassCount/month")
  public R<List<SecondClassCountVo>> getSecondClassCountByMonth(@RequestParam(required = false) Integer month) {
    return R.ok(raceInfoService.getSecondClassCountByMonth(month));
  }

  /**
   * 根据年份查询各学院第二课堂活动数量
   * 
   * @param year 没有即查询本年
   */
  @ApiOperation("查询今年各学院第二课堂活动数量")
  @GetMapping("/getSecondClassCount/year")
  public R<List<SecondClassCountVo>> getSecondClassCountByYear(@RequestParam(required = false) Integer year) {
    return R.ok(raceInfoService.getSecondClassCountByYear(year));
  }

  /**
   * 获取热门比赛TOP10
   * 
   * @return
   */
  @ApiOperation("查询热门比赛")
  @GetMapping("/getHotRaces")
  public R<List<HotRaceVo>> getHotRaces() {
    return R.ok(raceInfoService.getHotRace());
  }

  /**
   * 统计各年级学生的参与第二课堂的活动占比
   * 
   * @return
   */
  @ApiOperation("统计各年级学生的参与第二课堂的活动占比")
  @GetMapping("/getStudentGradeCount")
  public R<List<GradeStatistic>> getStudentGradeCount() {
    return R.ok(raceInfoService.getStudentGradeCount());
  }

  /**
   * 查询个人年度参与活动率
   * 
   * @param userId
   * @param grade
   * @return
   */
  @ApiOperation("查询个人年度参与活动率")
  @GetMapping("/participationRateByUser")
  public R<String> getUserAnnualParticipationRate(@RequestParam(value = "userId") Long userId,
      @RequestParam(value = "grade") int grade) {
    String result = raceInfoService.userAnnualParticipationRate(userId, grade);
    return R.ok(result);
  }

  /**
   * 查询学科竞赛教师指导项目数
   * 
   * @param userId
   * @return
   */
  @ApiOperation("查询学科竞赛教师指导项目数")
  @GetMapping("/getTeacherInstructCount/contest")
  public R<String> getTeacherInstructCountFromContest(@RequestParam(value = "userId") Long userId) {
    return R.ok(teamInfoService.getTeacherInstructCountFromContest(userId));
  }

  /**
   * 查询大创申报教师指导项目数
   * 
   * @param userId
   * @return
   */
  @ApiOperation("查询大创申报教师指导项目数")
  @GetMapping("/getTeacherInstructCount/innovation")
  public R<String> getTeacherInstructCountFromInnovation(@RequestParam(value = "userId") Long userId) {
    return R.ok(teamInfoService.getTeacherInstructCountFromInnovation(userId));
  }

  /**
   * 查询第二课堂教师指导项目数
   * 
   * @param userId
   * @return
   */
  @ApiOperation("查询第二课堂教师指导项目数")
  @GetMapping("/getTeacherInstructCount/second")
  public R<String> getTeacherInstructCountFromSecond(@RequestParam(value = "userId") Long userId) {
    return R.ok(raceInfoService.getTeacherInstructCountFromSecond(userId));
  }

  /**
   * 查询学生参与第二课堂活动数
   */
  @ApiOperation("查询学生参与第二课堂活动数")
  @GetMapping("/getStudentJoinCount/second")
  public R<Integer> getStudentInstructCountFromSecond(@RequestParam(value = "userId") Long userId) {
    return R.ok(teamMemberStudentService.getStudentJoinSecondCountByUserId(userId));
  }

  /**
   * 查询学生参与活动总数
   */
  @ApiOperation("查询学生参与活动总数")
  @GetMapping("/getStudentJoinCount/activity")
  public R<Integer> getStudentInstructCountFromActivity(@RequestParam(value = "userId") Long userId) {
    return R.ok(teamMemberStudentService.getStudentActivityByUserId(userId));
  }

  /**
   * 查询学生参与大创立项活动数
   */
  @ApiOperation("查询学生参与大创立项活动数")
  @GetMapping("/getStudentJoinCount/innovation")
  public R<Integer> getStudentInstructCountFromInnovation(@RequestParam(value = "userId") Long userId) {
    return R.ok(teamMemberStudentService.getStudentJoinInnovationCountByUserId(userId));
  }

  // /**
  // * 统计第二课堂热门活动类型TOP5
  // */
  // @ApiOperation("统计第二课堂热门活动类型TOP5")
  // @GetMapping("/getSecondHotRaceType")
  // public R<List<String>> getSecondHotRaceType() {
  // //查出热门TOP10活动
  // List<HotRaceVo> hotRace = raceInfoService.getHotRace();
  // List<String> result = new ArrayList<>();
  // //获取热门活动的类型
  // for (HotRaceVo hotRaceVo : hotRace) {
  // String secondType = hotRaceVo.getRaceDetail().getSecondType();
  // //根据value和type从字典获取到对应的活动类型
  // String type = sysDictDataService.selectDictLabel("second_type", secondType);
  // result.add(type);
  // }
  // //返回
  // return R.ok(result);
  // }

  @ApiOperation("统计第二课堂热门活动类型数量")
  @GetMapping("/getSecondHotRace")
  public R<List<HotRaceVo>> getSecondHotRace() {
    // 查出热门TOP10活动
    List<HotRaceVo> result = raceInfoService.getHot();
    // 返回
    return R.ok(result);
  }

  /**
   * 获取所带学生的竞赛状态
   * 
   * @param teacherId
   * @return
   */
  @ApiOperation("获取所带学生的竞赛参与信息")
  @GetMapping("/getStudentCompetitionStatus")
  public R<List<StudentCompetitionStatus>> getStudentCompetitionStatus(
      @RequestParam(value = "teacherId") Long teacherId) {
    List<StudentCompetitionStatus> result = statisticsService.getStudentCompetitionStatus(teacherId);

    return R.ok(result);
  }

  /**
   * 统计教师所带的团队技能数量
   * 
   * @return
   */
  @ApiOperation("根据团队Id查询技能数量")
  @GetMapping("/statisticsTeamSkill")
  public R<TeamSkillStatisticsVO> statisticsTeamSkill(@RequestParam(value = "teamId") Long teamId) {
    TeamSkillStatisticsVO teamSkillStatistics = teamSkillService.getTeamSkillStatistics(teamId);

    return R.ok(teamSkillStatistics);
  }

  /**
   * 统计总活动数（无筛选条件）
   * 
   * @return
   */
  @ApiOperation("统计总活动数")
  @GetMapping("/getActivityCount")
  public R<Integer> getActivityCount() {
    int count = raceInfoService.getActivityCount();
    return R.ok(count);
  }

  /**
   * 查询前五个热门第二课堂活动类型
   * 
   * @return
   */
  @ApiOperation("查询前五个热门第二课堂活动类型")
  @GetMapping("/getRecentlyPopularActType")
  public R<List<HotRaceVo>> getRecentlyPopularAct() {
    List<HotRaceVo> result = raceInfoService.getRecentlyPopularActType();

    return R.ok(result);
  }

  /**
   * 获取活动报名人数排行
   * 
   * @return
   */
  @ApiOperation("获取活动报名人数排行")
  @GetMapping("/getActPeopleCount")
  public R<List<ActPeopleCountVo>> getActPeopleCount() {
    List<ActPeopleCountVo> result = preSignStudentService.getActPeopleCount();

    return R.ok(result);
  }

  /**
   * 获取待审核项目
   * 
   * @param teacherId
   * @return
   */
  @ApiOperation("获取待审核项目")
  @GetMapping("/getUnderReviewProject")
  public R<List<RaceInfoVo>> getUnderReviewProject(@RequestParam(value = "teacherId") Long teacherId) {
    RaceInfo raceInfo = new RaceInfo();
    raceInfo.setCreateId(teacherId);
    List<RaceInfoVo> raceInfoList = raceInfoService.getRaceInfoList(raceInfo);

    return R.ok(raceInfoList);
  }

  /**
   * 获取指导总人数
   * 
   * @return
   */
  @ApiOperation("获取指导总人数")
  @GetMapping("/getTotalGuideNumber")
  public R<Integer> getTotalGuideNumber(@RequestParam(value = "teacherId") Long teacherId) {
    int count = teamMemberTeacherService.getTotalGuideNumber(teacherId);
    return R.ok(count);
  }

  /**
   * 获取指导下获奖数
   * 
   * @param teacherId
   * @return
   */
  @ApiOperation("获取指导下获奖数")
  @GetMapping("/getAwardCount")
  public R<Integer> getAwardCount(@RequestParam(value = "teacherId") Long teacherId) {
    int count = awardService.getAwardCount(teacherId);
    return R.ok(count);
  }

  /**
   * 获取老师项目完成率
   * 
   * @param teacherId
   * @return
   */
  @ApiOperation("获取老师项目完成率")
  @GetMapping("/getProjectCompletionRate")
  public R<Double> getProjectCompletionRate(@RequestParam(value = "teacherId") Long teacherId) {
    Double rate = teamInfoService.getProjectCompletionRate(teacherId);
    return R.ok(rate);
  }

  /**
   * 获取老师的近期指导记录（分页）
   * 
   * @param teacherId 老师ID
   * @param pageNum   页码
   * @param pageSize  每页数量
   * @return 指导记录列表
   */
  @ApiOperation("获取老师的近期指导记录")
  @GetMapping("/getRecentGuideRecords")
  public MyPageVo<List<TeacherGuideRecordVo>> getRecentGuideRecords(
      @RequestParam(value = "teacherId") Long teacherId,
      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
      @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
    List<TeacherGuideRecordVo> records = teamInfoService.getRecentGuideRecords(teacherId, pageNum, pageSize);
    int total = teamInfoService.getRecentGuideRecordsCount(teacherId);
    MyPageVo<List<TeacherGuideRecordVo>> pageVo = new MyPageVo<>();
    pageVo.setRows(records);
    pageVo.setTotal(total);
    return pageVo;
  }

  /**
   * 获取老师负责学生的能力分布
   * 
   * @param teacherId   老师ID
   * @param major       专业
   * @param sClass      班级
   * @param projectType 项目类型
   * @param year        学年
   * @return 能力分布列表
   */
  @ApiOperation("获取老师负责学生的能力分布")
  @GetMapping("/getStudentAbilityDistribution")
  public R<List<StudentAbilityDistributionVo>> getStudentAbilityDistribution(
      @RequestParam(value = "teacherId") Long teacherId,
      @RequestParam(value = "sClass", required = false, defaultValue = "") String sClass,
      @RequestParam(value = "projectType", required = false, defaultValue = "0") Integer projectType,
      @RequestParam(value = "year", required = false, defaultValue = "") String year) {
    List<StudentAbilityDistributionVo> distribution = scoreService.getStudentAbilityDistribution(teacherId,
        sClass, projectType, year);
    return R.ok(distribution);
  }

  /**
   * 获取老师负责学生的班级获奖情况对比
   * 
   * @param teacherId   老师ID
   * @param major       专业
   * @param sClass      班级
   * @param projectType 项目类型
   * @param year        学年
   * @return 班级获奖数量列表
   */
  @ApiOperation("获取老师负责学生的班级获奖情况对比")
  @GetMapping("/getClassAwardCount")
  public R<List<ClassAwardCountVo>> getClassAwardCount(
      @RequestParam(value = "teacherId") Long teacherId,
      @RequestParam(value = "sClass", required = false, defaultValue = "") String sClass,
      @RequestParam(value = "projectType", required = false, defaultValue = "0") Integer projectType,
      @RequestParam(value = "year", required = false, defaultValue = "") String year) {
    List<ClassAwardCountVo> classAwardCount = statisticsService.getClassAwardCount(teacherId,
        sClass, projectType, year);
    return R.ok(classAwardCount);
  }

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
  @ApiOperation("获取老师负责学生的学生培养明细表")
  @GetMapping("/getStudentCultivationDetail")
  public R<List<StudentCultivationDetailVo>> getStudentCultivationDetail(
      @RequestParam(value = "teacherId") Long teacherId,
      @RequestParam(value = "major", required = false, defaultValue = "0") Integer major,
      @RequestParam(value = "sClass", required = false, defaultValue = "") String sClass,
      @RequestParam(value = "projectType", required = false, defaultValue = "0") Integer projectType,
      @RequestParam(value = "year", required = false, defaultValue = "") String year) {
    List<StudentCultivationDetailVo> studentCultivationDetail = statisticsService.getStudentCultivationDetail(teacherId,
        major,
        sClass, projectType, year);
    return R.ok(studentCultivationDetail);
  }

  /**
   * 获取老师负责的项目/竞赛管理明细表
   * 
   * @param teacherId 老师ID
   * @return 项目/竞赛管理明细表列表
   */
  @ApiOperation("获取老师负责的项目/竞赛管理明细表")
  @GetMapping("/getProjectCompetitionDetail")
  public R<List<ProjectCompetitionDetailVo>> getProjectCompetitionDetail(
      @RequestParam(value = "teacherId") Long teacherId) {
    List<ProjectCompetitionDetailVo> projectCompetitionDetail = statisticsService
        .getProjectCompetitionDetail(teacherId);
    return R.ok(projectCompetitionDetail);
  }

  /**
   * 获取老师的历年指导成果
   * 
   * @param teacherId 老师ID
   * @return 历年指导成果列表
   */
  @ApiOperation("获取老师的历年指导成果")
  @GetMapping("/getYearlyGuidanceResult")
  public R<List<YearlyGuidanceResultVo>> getYearlyGuidanceResult(
      @RequestParam(value = "teacherId") Long teacherId) {
    List<YearlyGuidanceResultVo> yearlyGuidanceResult = statisticsService.getYearlyGuidanceResult(teacherId);
    return R.ok(yearlyGuidanceResult);
  }

  /**
   * 获取老师负责项目/竞赛的获奖等级分布
   * 
   * @param teacherId 老师ID
   * @return 获奖等级分布列表
   */
  @ApiOperation("获取老师负责项目/竞赛的获奖等级分布")
  @GetMapping("/getAwardLevelDistribution")
  public R<List<AwardLevelDistributionVo>> getAwardLevelDistribution(
      @RequestParam(value = "teacherId") Long teacherId) {
    List<AwardLevelDistributionVo> awardLevelDistribution = statisticsService.getAwardLevelDistribution(teacherId);
    return R.ok(awardLevelDistribution);
  }

  /**
   * 获取学生创新活动趋势
   * 
   * @param teacherId 老师ID
   * @return 学生创新活动趋势列表
   */
  @ApiOperation("获取学生创新活动趋势")
  @GetMapping("/getStudentInnovationTrend")
  public R<List<StudentInnovationTrendVo>> getStudentInnovationTrend(
      @RequestParam(value = "teacherId") Long teacherId) {
    List<StudentInnovationTrendVo> studentInnovationTrend = statisticsService.getStudentInnovationTrend(teacherId);
    return R.ok(studentInnovationTrend);
  }
}
