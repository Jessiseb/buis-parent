package com.buis.web.controller.api;

import com.buis.common.core.domain.R;
import com.buis.domain.RaceInfo;
import com.buis.domain.TeamInfo;
import com.buis.domain.TeamMemberStudent;
import com.buis.dto.GradeStatistic;
import com.buis.service.ITeamInfoService;
import com.buis.service.ITeamMemberStudentService;
import com.buis.service.IUserInfoService;
import com.buis.service.impl.RaceInfoServiceImpl;
import com.buis.vo.RaceInfoVo;
import com.buis.vo.SecondClassCountVo;
import com.buis.vo.TeamInfoVo;
import com.buis.vo.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@Api(tags = "统计Api")
@RestController
@RequestMapping("/api/statistics")
public class StatisticsApiController {


    @Autowired
    private RaceInfoServiceImpl raceInfoService;
    @Autowired
    private ITeamInfoService teamInfoService;
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private ITeamMemberStudentService teamMemberStudentService;


    /**
     * 查询本月各学院第二课堂活动数量
     */
    @ApiOperation("查询本月各学院第二课堂活动数量")
    @GetMapping("/getSecondClassCount/month")
    public List<SecondClassCountVo> getSecondClassCountByMonth() {
        return raceInfoService.getSecondClassCountByMonth();
    }

    /**
     * 查询今年各学院第二课堂活动数量
     */
    @ApiOperation("查询今年各学院第二课堂活动数量")
    @GetMapping("/getSecondClassCount/year")
    public List<SecondClassCountVo> getSecondClassCountByYear() {
        return raceInfoService.getSecondClassCountByYear();
    }


    /**
     * 统计第二课堂的活动总量
     */
    @ApiOperation("第二课堂的活动总量")
    @GetMapping("/getSecondClassCount")
    public R<Integer> getSecondClassCount() {
        RaceInfo raceInfo = new RaceInfo();
        List<RaceInfoVo> raceInfoVos = raceInfoService.selectRaceInfoList(raceInfo);
        if (raceInfoVos == null) {
            raceInfoVos = Collections.emptyList();
        }
        return R.ok(raceInfoVos.size());
    }


    /**
     * 统计各年级学生的参与第二课堂的活动占比
     */
    @ApiOperation("统计各年级学生的参与第二课堂的活动占比")
    @GetMapping("/getStudentGradeCount")
    public R<List<GradeStatistic>> getStudentGradeCount() {
        //一. 获取所有参与第二课堂活动的用户ids
        TeamInfo teamInfo = new TeamInfo();
        List<TeamInfoVo> teamInfoVos = teamInfoService.selectTeamInfoList(teamInfo).stream()
                .filter(Objects::nonNull).collect(Collectors.toList());
        //所有用户id
        List<Long> userIds = new ArrayList<>();

        for (TeamInfoVo teamInfoVo : teamInfoVos) {
            Long race = teamInfoVo.getRace();
            //根据race获取到对应的比赛信息,并判断是否为第二课堂
            if (race != null) {
                RaceInfo raceInfo = raceInfoService.selectRaceInfoById(race);
                if (raceInfo != null && raceInfo.getParentTid() == 5) {
                    //是第二课堂
                    //根据这个比赛id获取到队伍成员信息
                    List<TeamMemberStudent> teamMemberStudents = teamMemberStudentService.selectTeamMemberStudentByTeamId(teamInfoVo.getId());
                   //去重
                    for (TeamMemberStudent teamMemberStudent : teamMemberStudents) {
                        userIds.add(teamMemberStudent.getStudent());
                    }

                    userIds.add(teamInfoVo.getLeader());
                    //userIds去重
                    userIds = userIds.stream().distinct().collect(Collectors.toList());
                }
            }

        }


        //根据leaderIds查询到对应的用户信息
        List<UserInfoVo> userInfoVos = Optional.ofNullable(userInfoService.selectUserInfoByIds(userIds))
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        //提取年级并统计
        Map<String, Long> gradeCount = userInfoVos.stream()
                .filter(u -> u.getNumber() != null)
                .collect(Collectors.groupingBy(
                        u -> u.getNumber().substring(0, 4), // 假设学号前4位是年级
                        Collectors.counting()
                ));


        //计算总参与人数和占比
        long total = gradeCount.values().stream().mapToLong(Long::longValue).sum();
        if (total == 0) {
            return R.ok(Collections.emptyList()); // 或抛出业务异常
        }

        return R.ok(gradeCount.entrySet().stream()
                .map(entry -> GradeStatistic.builder()
                        .grade(entry.getKey())
                        .count(entry.getValue())
                        .percentage(entry.getValue() / (double) total * 100)
                        .build())
                .collect(Collectors.toList()));

    }

}
