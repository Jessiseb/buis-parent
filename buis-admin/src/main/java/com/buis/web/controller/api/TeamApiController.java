package com.buis.web.controller.api;

import cn.dev33.satoken.stp.StpUtil;
import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.R;
import com.buis.common.core.domain.model.LoginUser;
import com.buis.common.core.page.TableDataInfo;
import com.buis.common.exception.ServiceException;
import com.buis.domain.TeamInfo;
import com.buis.domain.TeamMemberTeacher;
import com.buis.domain.UserInfo;
import com.buis.dto.TeamQueryDTO;
import com.buis.service.ITeamMemberStudentService;
import com.buis.service.ITeamMemberTeacherService;
import com.buis.service.IUserInfoService;
import com.buis.service.impl.TeamInfoServiceImpl;
import com.buis.service.impl.UserInfoServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


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
    public TableDataInfo list(@PathVariable Long race, @PathVariable Long team) {
        startPage();
        List<UserInfo> result = teamMemberStudentService.selectTeamMemberStudentQueryList(race, team);
        return getDataTable(result);
    }

    @ApiOperation("查询我(老师)指导的队伍列表")
    @GetMapping("/listTeam/my/instruct")
    public R<List<TeamInfo>> listTeamMyInstruct(TeamQueryDTO teamQueryDTO) {
        //判断当前用户身份
        //1.找出当前用户信息
        UserInfo loginUser = userInfoService.getLoginUser();
        //如果title为null,说明不是老师,不能查询
        Long title = loginUser.getTitle();
        if (title == null){
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

}
