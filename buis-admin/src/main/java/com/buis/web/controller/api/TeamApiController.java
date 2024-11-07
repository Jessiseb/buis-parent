package com.buis.web.controller.api;


import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.R;
import com.buis.common.core.page.MyPageVo;
import com.buis.common.core.page.TableDataInfo;
import com.buis.common.utils.SecurityUtils;
import com.buis.domain.TeamInfo;
import com.buis.service.ITeamMemberStudentService;
import com.buis.service.ITeamMemberTeacherService;
import com.buis.service.impl.TeamInfoServiceImpl;
import com.buis.vo.TeamInfoVo;
import com.buis.vo.UserInfoVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;


/**
 * 队伍管理
 */
@Api(tags = "队伍管理Api")
@RestController
@RequestMapping("/api/team")
public class TeamApiController extends BaseController{
    @Autowired
    private ITeamMemberStudentService teamMemberStudentService;
    @Autowired
    private ITeamMemberTeacherService teamMemberTeacherService;
    @Autowired
    private TeamInfoServiceImpl teamInfoService;

    @ApiOperation("根据队伍id查询队伍成员列表")
    @GetMapping("/Member/{team}")
    public R<HashMap<String, List<UserInfoVo>>> getMemberStudentByTeamId(
                              @ApiParam(name = "team", value = "队伍id", required = true)@PathVariable("team") Long team) {
        List<UserInfoVo> student = teamMemberStudentService.getMemberStudentByTeamId(team);
        List<UserInfoVo> teacher = teamMemberTeacherService.getTeachersByRaceId(team);
        HashMap<String, List<UserInfoVo>> map = new HashMap<>();
        map.put("student", student);
        map.put("teacher", teacher);
        return R.ok(map);
    }


    /**
     * 查询队伍信息列表
     */
    @ApiOperation("带条件分页查询队伍列表")
    @PostMapping("/teamList")
    public MyPageVo<List<TeamInfoVo>> list(@RequestBody TeamInfo teamInfo) {
        startPage();
        List<TeamInfoVo> list = teamInfoService.selectTeamInfoList(teamInfo);
        return new MyPageVo(list, (int) new PageInfo(list).getTotal());
    }


    /**
     * 根据老师id查询老师指导的队伍列表
     *
     * @return 返回结果
     */
    @ApiOperation("根据老师id查询老师指导的队伍列表")
    @GetMapping("/guide")
    public R<List<TeamInfoVo>> guide() {
        Long userId = SecurityUtils.getFrontLoginUserId();
        List<TeamInfoVo> teamInfoVos = teamMemberTeacherService.selectJoinTeamByTeacherId(userId);
        return R.ok(teamInfoVos);
    }

}
