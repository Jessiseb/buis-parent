package com.buis.web.controller.api;

import com.buis.common.core.domain.R;
import com.buis.domain.UserInfo;
import com.buis.service.ITeamMemberTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.buis.common.utils.PageUtils.startPage;

/**
 * 比赛信息
 */
@Api(tags = "比赛信息Api")
@RestController
@RequestMapping("/api/race")
public class RaceInfoApiController {
    @Resource
    private ITeamMemberTeacherService teamMemberTeacherService;

    @ApiOperation("根据比赛id查询指导老师信息")
    @PostMapping("/listTeachers/{RaceId}")
    public R<List<UserInfo>> listTeachersByRaceId(@PathVariable("RaceId") Long id) {
        //开始分页
        startPage();

        //根据比赛id查出队伍老师表中的老师id
        List<Integer> teacherIds = teamMemberTeacherService.listTeacherIdByRaceId(id);

        //去重
        List<Integer> Ids = teacherIds.stream().distinct().collect(Collectors.toList());

        //根据老师ids查出所有老师的信息
        List<UserInfo> userInfos = teamMemberTeacherService.listTeachersByIds(Ids);

        //返回结果
        return R.ok(userInfos);
    }


}
