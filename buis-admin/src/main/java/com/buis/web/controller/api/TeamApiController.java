package com.buis.web.controller.api;


import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.R;
import com.buis.common.core.page.MyPageVo;
import com.buis.common.exception.ServiceException;
import com.buis.common.utils.SecurityUtils;
import com.buis.domain.TeamInfo;
import com.buis.domain.TeamMemberStudent;
import com.buis.domain.TeamMemberTeacher;
import com.buis.dto.TeamMemberDto;
import com.buis.service.IRaceInfoService;
import com.buis.service.ITeamInfoService;
import com.buis.service.ITeamMemberStudentService;
import com.buis.service.ITeamMemberTeacherService;
import com.buis.service.impl.TeamInfoServiceImpl;
import com.buis.vo.TeamInfoVo;
import com.buis.vo.TeamMemberStudentVo;
import com.buis.vo.TeamMemberTeacherVo;
import com.buis.vo.UserInfoVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class TeamApiController extends BaseController {
    @Autowired
    private ITeamMemberStudentService teamMemberStudentService;
    @Autowired
    private ITeamMemberTeacherService teamMemberTeacherService;
    @Autowired
    private ITeamInfoService teamInfoService;
    @Autowired
    private IRaceInfoService raceInfoService;

    /**
     * 新增用户与队伍互选关系
     *
     * @param teamMemberDto
     * @return
     */
    @ApiOperation("新增用户与队伍互选关系")
    @PostMapping("/addTeamMember/{isTeacher}")
    public R addInvitation(
            @ApiParam(name = "isTeacher", value = "用户类型，0学生，1老师", required = true) @PathVariable(value = "isTeacher") Long isTeacher,
            @RequestBody TeamMemberDto teamMemberDto) {

        if (isTeacher != null) {
            if (isTeacher == 0) { // 用户是学生
                TeamMemberStudent teamMemberStudent = buildTeamMemberStudent(teamMemberDto);
                teamMemberStudentService.insertTeamMemberStudent(teamMemberStudent);
            } else if (isTeacher == 1) { // 用户是老师
                TeamMemberTeacher teamMemberTeacher = buildTeamMemberTeacher(teamMemberDto);
                teamMemberTeacherService.insertTeamMemberTeacher(teamMemberTeacher);
            }
        }
        return R.ok();
    }

    /**
     * 修改用户与队伍互选关系
     * @param isTeacher
     * @param teamMemberDto
     * @return
     */
    @ApiOperation("修改用户与队伍互选关系")
    @PutMapping("/updateTeamMember/{isTeacher}/{id}")
    public R updateTeamMember(
            @ApiParam(name = "isTeacher", value = "用户类型，0学生，1老师", required = true) @PathVariable(value = "isTeacher") Long isTeacher,
             @ApiParam(name = "id", value = "记录ID", required = true) @PathVariable(value = "id") Long id,
            @RequestBody TeamMemberDto teamMemberDto) {

        if (isTeacher != null) {
            if (isTeacher == 0) { // 用户是学生
                TeamMemberStudent teamMemberStudent = buildTeamMemberStudent(teamMemberDto);
                teamMemberStudent.setId(id);
                teamMemberStudentService.updateTeamMemberStudent(teamMemberStudent);
            } else if (isTeacher == 1) { // 用户是老师
                TeamMemberTeacher teamMemberTeacher = buildTeamMemberTeacher(teamMemberDto);
                teamMemberTeacher.setId(id);
                teamMemberTeacherService.updateTeamMemberTeacher(teamMemberTeacher);
            }
        }
        return R.ok();
    }

    /**
     * 构建 TeamMemberStudent 对象
     */
    private TeamMemberStudent buildTeamMemberStudent(TeamMemberDto teamMemberDto) {
        TeamMemberStudent teamMemberStudent = new TeamMemberStudent();
        teamMemberStudent.setStudent(teamMemberDto.getUser());
        teamMemberStudent.setTeam(teamMemberDto.getTeam());
        teamMemberStudent.setDecider(teamMemberDto.getDecisionRole());
        teamMemberStudent.setReason(teamMemberDto.getReason());
        teamMemberStudent.setRace(teamMemberDto.getRace());
        teamMemberStudent.setIsChoosed(teamMemberDto.getIsChoosed());
        return teamMemberStudent;
    }

    /**
     * 构建 TeamMemberTeacher 对象
     */
    private TeamMemberTeacher buildTeamMemberTeacher(TeamMemberDto teamMemberDto) {
        TeamMemberTeacher teamMemberTeacher = new TeamMemberTeacher();
        teamMemberTeacher.setTeacher(teamMemberDto.getUser());
        teamMemberTeacher.setTeam(teamMemberDto.getTeam());
        teamMemberTeacher.setDecider(teamMemberDto.getDecisionRole());
        teamMemberTeacher.setReason(teamMemberDto.getReason());
        teamMemberTeacher.setRace(teamMemberDto.getRace());
        teamMemberTeacher.setIsChoosed(teamMemberDto.getIsChoosed());
        return teamMemberTeacher;
    }

    @ApiOperation("删除用户与队伍互选关系")
    @DeleteMapping("/deleteTeamMember/{team}/{user}/{isTeacher}")
    public R delectTeamMember(
            @ApiParam(name = "team", value = "队伍id", required = true) @PathVariable(value = "team") Long team,
            @ApiParam(name = "user", value = "用户id", required = true) @PathVariable(value = "user") Long user,
            @ApiParam(name = "isTeacher", value = "用户类型，0学生，1老师", required = true) @PathVariable(value = "isTeacher") Long isTeacher) {
        if (isTeacher != null && isTeacher == 0) { // 用户是学生
            teamMemberStudentService.deleteTeamMemberStudentByTeamAndStudent(team, user);
        } else if (isTeacher != null && isTeacher == 1) { // 用户是老师
            teamMemberTeacherService.deleteTeamMemberTeacherByTeamAndTeacher(team, user);
        }
        return R.ok();
    }

    /**
     * 带条件分页查询学生与队伍互选邀请
     * @param teamMemberStudent
     * @return
     */
    @ApiOperation("带条件分页查询学生与队伍互选邀请")
    @PostMapping("/getTeamMemberStudent")
    public MyPageVo<List<TeamMemberStudentVo>> getTeamMemberStudent(@RequestParam(required = false)TeamMemberStudent teamMemberStudent) {
        startPage();
        List<TeamMemberStudentVo> list = teamMemberStudentService.selectTeamMemberStudentList(teamMemberStudent);
        return new MyPageVo<>(list);
    }

    /**
     * 带条件分页查询老师与队伍互选邀请
     * @param teamMemberTeacher
     * @return
     */
    @ApiOperation("带条件分页查询老师与队伍互选邀请")
    @PostMapping("/getTeamMemberTeacher")
    public MyPageVo<List<TeamMemberTeacherVo>> getTeamMemberTeacher(@RequestParam(required = false)TeamMemberTeacher teamMemberTeacher) {
        startPage();
        List<TeamMemberTeacherVo> list = teamMemberTeacherService.selectTeamMemberTeacherList(teamMemberTeacher);
        return new MyPageVo<>(list);
    }

    /**
     * 根据队伍id查询队伍成员列表
     * @param pageNum
     * @param pageSize
     * @param team
     * @return
     */
    @ApiOperation("根据队伍id查询队伍成员列表")
    @GetMapping("/Member/{team}")
    public R<HashMap<String, List<UserInfoVo>>> getMemberStudentByTeamId(
            @ApiParam(name = "pageNum", value = "查询页数", required = true) @RequestParam Integer pageNum,
            @ApiParam(name = "pageSize", value = "查询数量", required = true) @RequestParam Integer pageSize,
            @ApiParam(name = "team", value = "队伍id", required = true) @PathVariable("team") Long team) {
        List<UserInfoVo> student = teamMemberStudentService.getMemberStudentByTeamId(team);
        List<UserInfoVo> teacher = teamMemberTeacherService.getMemberTeachersByTeamId(team);
        HashMap<String, List<UserInfoVo>> map = new HashMap<>();
        map.put("student", student);
        map.put("teacher", teacher);
        return R.ok(map);
    }


    /**
     * 查询队伍信息列表
     */
    @ApiOperation("带条件分页查询队伍列表")
    @PostMapping("/list")
    public MyPageVo<List<TeamInfoVo>> list(
            @ApiParam(name = "pageNum", value = "查询页数", required = true) @RequestParam Integer pageNum,
            @ApiParam(name = "pageSize", value = "查询数量", required = true) @RequestParam Integer pageSize,
            @RequestBody(required = false) TeamInfo teamInfo) {
        startPage();
        List<TeamInfoVo> list = teamInfoService.selectTeamInfoList(teamInfo);
        return new MyPageVo(list);
    }


    /**
     * 根据老师id查询老师指导的队伍列表
     *
     * @return 返回结果
     */
    @ApiOperation("带条件分页查询老师指导的队伍列表")
    @PostMapping("/guide")
    public MyPageVo<List<TeamInfoVo>> guide(@ApiParam(name = "pageNum", value = "查询页数", required = true) @RequestParam Integer pageNum,
                                            @ApiParam(name = "pageSize", value = "查询数量", required = true) @RequestParam Integer pageSize,
                                            @ApiParam(name = "race", value = "比赛id", required = false) @RequestParam(required = false) Long race,
                                                                                        @ApiParam(name = "name", value = "比赛名字", required = false) @RequestParam(required = false) Long name
                                            ) {
        startPage();
        Long userId = SecurityUtils.getFrontLoginUserId();
        List<TeamInfoVo> teamInfoVos = teamMemberTeacherService.selectJoinTeamByTeacherId(userId,race,name);
        return new MyPageVo(teamInfoVos);
    }
    /**
     * 新增队伍信息
     *
     * @param teamInfo 队伍信息对象
     * @return R
     */
    @ApiOperation("新增队伍信息")
    @PostMapping
    public R addTeamInfo(@RequestBody TeamInfo teamInfo) {
        Long userId = SecurityUtils.getFrontLoginUserId();
        teamInfo.setLeader(userId);
        int insertTeamInfo = teamInfoService.insertTeamInfo(teamInfo);
        return R.ok(insertTeamInfo, "操作成功");
    }


    /**
     * 修改队伍信息
     *
     * @param teamInfo 队伍信息对象
     * @param id       队伍ID
     * @return R
     */
    @ApiOperation("修改队伍信息")
    @PutMapping("/{id}")
    public R editTeamInfo(@RequestBody TeamInfo teamInfo, @ApiParam(name = "id", value = "队伍id", required = true)@PathVariable Long id) {
        teamInfo.setId(id);
        int result = teamInfoService.updateTeamInfo(teamInfo);
        return R.ok(result, "操作成功");
    }


    /**
     * 删除队伍信息
     *
     * @param id 队伍ID
     * @return R
     */
    @ApiOperation("删除队伍信息")
    @DeleteMapping("/{id}")
    public R removeTeamInfo(@ApiParam(name = "id", value = "队伍id", required = true)@PathVariable Long id) {
        int result = teamInfoService.deleteTeamInfoById(id);
        return R.ok(result, "操作成功");
    }

}
