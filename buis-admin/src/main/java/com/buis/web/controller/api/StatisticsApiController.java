package com.buis.web.controller.api;

import com.buis.common.core.domain.R;
import com.buis.domain.RaceInfo;
import com.buis.domain.TeamInfo;
import com.buis.domain.TeamMemberStudent;
import com.buis.dto.GradeStatistic;
import com.buis.service.IRaceInfoService;
import com.buis.service.ITeamInfoService;
import com.buis.service.ITeamMemberStudentService;
import com.buis.service.IUserInfoService;
import com.buis.vo.HotRaceVo;
import com.buis.vo.SecondClassCountVo;
import com.buis.vo.TeamInfoVo;
import com.buis.vo.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

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
    private IUserInfoService userInfoService;


    /**
     * 统计各年级学生的参与活动
     */

    /**
     * 根据月份查询各学院第二课堂活动数量
     * @param month 没有即查询本月
     */
    @ApiOperation("查询本月各学院第二课堂活动数量")
    @GetMapping("/getSecondClassCount/month")
    public R<List<SecondClassCountVo>> getSecondClassCountByMonth(@RequestParam(required = false) Integer month) {
        return R.ok(raceInfoService.getSecondClassCountByMonth(month));
    }

    /**
     * 根据年份查询各学院第二课堂活动数量
     * @param year 没有即查询本年
     */
    @ApiOperation("查询今年各学院第二课堂活动数量")
    @GetMapping("/getSecondClassCount/year")
    public R<List<SecondClassCountVo>> getSecondClassCountByYear(@RequestParam(required = false) Integer year) {
        return R.ok(raceInfoService.getSecondClassCountByYear(year));
    }

    /**
     * 获取热门比赛TOP10
     * @return
     */
    @ApiOperation("查询热门比赛")
    @GetMapping("/getHotRaces")
    public R<List<HotRaceVo>> getHotRaces() {
        return R.ok(raceInfoService.getHotRace());
    }


    /**
     * 统计各年级学生的参与第二课堂的活动占比
     * @return
     */
    @ApiOperation("统计各年级学生的参与第二课堂的活动占比")
    @GetMapping("/getStudentGradeCount")
    public R<List<GradeStatistic>> getStudentGradeCount() {
        return R.ok(raceInfoService.getStudentGradeCount());
    }


    @ApiOperation("查询个人年度参与活动率")
    @GetMapping("/participationRateByUser")
    public R<String> getUserAnnualParticipationRate(@RequestParam(value = "userId") Long userId,
                                                    @RequestParam(value = "grade") int grade) {
        String result = raceInfoService.userAnnualParticipationRate(userId, grade);
        return R.ok(result);
    }

    @ApiOperation("查询学科竞赛教师指导项目数")
    @GetMapping("/getTeacherInstructCount/contest")
    public R<String> getTeacherInstructCountFromContest(@RequestParam(value = "userId") Long userId) {
        return R.ok(teamInfoService.getTeacherInstructCountFromContest(userId));
    }

    @ApiOperation("查询大创申报教师指导项目数")
    @GetMapping("/getTeacherInstructCount/innovation")
    public R<String> getTeacherInstructCountFromInnovation(@RequestParam(value = "userId") Long userId) {
        return R.ok(teamInfoService.getTeacherInstructCountFromInnovation(userId));
    }

    @ApiOperation("查询第二课堂教师指导项目数")
    @GetMapping("/getTeacherInstructCount/second")
    public R<String> getTeacherInstructCountFromSecond(@RequestParam(value = "userId") Long userId) {
        return R.ok(raceInfoService.getTeacherInstructCountFromSecond(userId));
    }
}
