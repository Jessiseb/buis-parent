package com.buis.web.controller.api;


import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.R;
import com.buis.common.core.page.TableDataInfo;
import com.buis.common.utils.SecurityUtils;
import com.buis.domain.TeamInfo;
import com.buis.service.ITeamMemberStudentService;
import com.buis.service.ITeamMemberTeacherService;
import com.buis.service.impl.TeamInfoServiceImpl;
import com.buis.vo.TeamInfoVo;
import com.buis.vo.UserInfoVo;
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
    public R getMemberStudentByTeamId(
                              @ApiParam(name = "team", value = "队伍id", required = true)@PathVariable("team") Long team) {
        List<UserInfoVo> student = teamMemberStudentService.getMemberStudentByTeamId(team);
        List<UserInfoVo> teacher = teamMemberTeacherService.getTeachersByRaceId(team);
        HashMap<Object, Object> map = new HashMap<>();
        map.put("student", student);
        map.put("teacher", teacher);
        return R.ok(map);
    }


    /**
     * 查询队伍信息列表
     */
    @ApiOperation("带条件分页查询队伍列表")
    @PostMapping("/teamList")
    public TableDataInfo list(@RequestBody TeamInfo teamInfo) {
        startPage();
        List<TeamInfoVo> list = teamInfoService.selectTeamInfoList(teamInfo);
        return getDataTable(list);
    }


    /**
     * 根据老师id查询老师指导的队伍列表
     * @return 返回结果
     */
    @ApiOperation("根据老师id查询老师指导的队伍列表")
    @GetMapping("/guide")
    public R guide() {
        Long userId = SecurityUtils.getFrontLoginUserId();
        List<TeamInfoVo> teamInfoVos = teamMemberTeacherService.selectJoinTeamByTeacherId(userId);
        return R.ok(teamInfoVos);
    }


    /**
     * 新增队伍信息
     * @param teamInfo 队伍信息对象
     * @return R
     */
    @ApiOperation("新增队伍信息")
    @PostMapping("/teams")
    public R addTeamInfo(@RequestBody TeamInfo teamInfo){
        try {
            int insertTeamInfo = teamInfoService.insertTeamInfo(teamInfo);
            return R.ok(insertTeamInfo, "操作成功");
        }catch (Exception e){
            return R.fail("新增队伍信息失败！");
        }
    }


    /**
     * 修改队伍信息
     * @param teamInfo 队伍信息对象
     * @param id 队伍ID
     * @return R
     */
    @ApiOperation("修改队伍信息")
    @PutMapping("/teams/{id}")
    public R editTeamInfo(@RequestBody TeamInfo teamInfo,@PathVariable Long id){
        try {
            teamInfo.setId(id);
            int result = teamInfoService.updateTeamInfo(teamInfo);
            return R.ok(result, "操作成功");
        }catch (Exception e){
            return R.fail("修改队伍信息失败！");
        }
    }


    /**
     * 删除队伍信息
     * @param id 队伍ID
     * @return R
     */
    @ApiOperation("删除队伍信息")
    @DeleteMapping("/teams/{id}")
    public R removeTeamInfo(@PathVariable Long id){
        try {
            int result = teamInfoService.deleteTeamInfoById(id);
            return R.ok(result, "操作成功");
        }catch (Exception e){
             return R.fail("删除队伍信息失败！");
        }
    }

}
