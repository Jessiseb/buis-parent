package com.buis.web.controller.api;


import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.R;
import com.buis.common.core.page.TableDataInfo;
import com.buis.common.exception.ServiceException;
import com.buis.domain.TeamInfo;
import com.buis.domain.TeamMemberStudent;
import com.buis.domain.TeamMemberTeacher;
import com.buis.domain.UserInfo;
import com.buis.dto.TeamQueryDto;
import com.buis.service.ITeamMemberStudentService;
import com.buis.service.ITeamMemberTeacherService;
import com.buis.service.IUserInfoService;
import com.buis.service.impl.TeamInfoServiceImpl;
import com.buis.vo.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


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
    private IUserInfoService userInfoService;
    @Autowired
    private TeamInfoServiceImpl teamInfoService;

    @ApiOperation("查询队伍成员学生列表")
    @GetMapping("/listMember/student/{race}/{team}")
    public TableDataInfo list(@ApiParam(name = "race", value = "比赛id", required = true)@PathVariable("race") Long race,
                              @ApiParam(name = "team", value = "队伍id", required = true)@PathVariable("team") Long team) {
        startPage();
        List<UserInfoVo> result = teamMemberStudentService.selectTeamMemberStudentQueryList(race, team);
        return getDataTable(result);
    }

//region 查询队伍
    /**
     * 查询所有队伍列表->也用户所有用户查询
     *
     * @param teamQueryDTO 查询参数
     * @return 返回结果
     */
    @ApiOperation("查询所有队伍列表")
    @GetMapping("/listTeams")
    public R<List<TeamInfo>> listTeams(@ApiParam(name = "teamQueryDTO", value = "查询队伍信息", required = false)TeamQueryDto teamQueryDTO) {
        startPage();
        List<TeamInfo> list = teamInfoService.listTeams(teamQueryDTO);
        return R.ok(list);
    }


    /**
     * 查询我(老师)指导的队伍列表
     *
     * @param teamQueryDTO 查询参数
     * @return 返回结果
     */
    @ApiOperation("查询我(老师)指导的队伍列表")
    @GetMapping("/listTeam/my/instruct")
    public R<List<TeamInfo>> listTeamMyInstruct(@ApiParam(name = "teamQueryDTO", value = "查询队伍信息", required = false)TeamQueryDto teamQueryDTO) {
        //判断当前用户身份
        //1.找出当前用户信息
        UserInfo loginUser = userInfoService.getLoginUser();
        //如果title为null,说明不是老师,不能查询
        Long title = loginUser.getTitle();
        if (title == null) {
            throw new ServiceException("无权限");
        }
        //否则执行查询的方法
        //根据当前用户id查询所在的队伍
        List<TeamMemberTeacher> teamMemberTeacherList = teamMemberTeacherService.selectTeamMyInstruct(loginUser.getId());
        //根据队伍老师表,获取到队伍ids
        List<Long> teamIds = teamMemberTeacherList.stream().map(TeamMemberTeacher::getTeam).collect(Collectors.toList());
        //根据ids查询队伍信息
        teamQueryDTO.setIdList(teamIds);
        List<TeamInfo> teamInfos = teamInfoService.listTeams(teamQueryDTO);
        return R.ok(teamInfos);
    }


    /**
     * 查询我创建的队伍列表
     *
     * @param teamQueryDTO 查询参数
     * @return 返回结果
     */
    @ApiOperation("查询我创建的队伍列表")
    @GetMapping("/listTeam/my/create")
    public R<List<TeamInfo>> listTeamMyCreate(@ApiParam(name = "teamQueryDTO", value = "查询队伍信息", required = false)TeamQueryDto teamQueryDTO) {
        //获取当前登录用户
        UserInfo loginUser = userInfoService.getLoginUser();
        //获取登录用户id
        Long userId = loginUser.getId();
        Long title = loginUser.getTitle();

        List<TeamInfo> myTeamsAsLeader = null;
        //判断是老师还是学生
        if (title == null) {
            //是学生
            //根据当前用户id查询所在的队伍
            List<TeamMemberStudent> teamMemberStudentsList = teamMemberStudentService.selectTeamMyCreate(loginUser.getId());

            //根据学生队伍表,获取到队伍ids
            List<Long> teamIds = teamMemberStudentsList.stream().map(TeamMemberStudent::getTeam).collect(Collectors.toList());

            //根据队伍ids查询到队伍信息
            teamQueryDTO.setIdList(teamIds);
            List<TeamInfo> teamInfos = teamInfoService.listTeams(teamQueryDTO);

            //获取到所加入队伍的所有队长id
            List<Long> LeaderIds = teamInfos.stream().map(TeamInfo::getLeader).collect(Collectors.toList());

            //判断本用户id与队长id是否对应,对应说明是该队伍的队长,则是我创建的队伍
            myTeamsAsLeader = teamInfos
                    .stream()
                    .filter(teamInfo -> userId.equals(LeaderIds))
                    .collect(Collectors.toList());
        } else {
            //是老师
            //根据当前用户id查询所在的队伍
            List<TeamMemberTeacher> teamMemberTeacherList = teamMemberTeacherService.selectTeamMyInstruct(loginUser.getId());

            //根据队伍老师表,获取到队伍ids
            List<Long> teamIds = teamMemberTeacherList.stream().map(TeamMemberTeacher::getTeam).collect(Collectors.toList());

            //根据队伍ids获得所有加入的队伍
            teamQueryDTO.setIdList(teamIds);

            //本用户已加入的队伍
            List<TeamInfo> teamInfos = teamInfoService.listTeams(teamQueryDTO);

            //获取到所有的队长id
            List<Long> LeaderIds = teamInfos.stream().map(TeamInfo::getLeader).collect(Collectors.toList());
            //根据队长id和本用户id做对比,一样的说明本用户就是队长-->查出所在的队伍并返回
//            teamInfoService.selectTeamMyCreate(LeaderIds);
            myTeamsAsLeader = teamInfos.stream()
                    .filter(teamInfo -> userId.equals(LeaderIds))
                    .collect(Collectors.toList());
        }
        return R.ok(myTeamsAsLeader);
    }

    //endregion



}
