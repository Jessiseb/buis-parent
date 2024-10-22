package com.buis.web.controller.api;

import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.model.LoginUser;
import com.buis.common.core.page.TableDataInfo;
import com.buis.common.exception.ServiceException;
import com.buis.domain.UserInfo;
import com.buis.dto.TeamMemberStudentQueryDTO;
import com.buis.dto.TeamQueryDTO;
import com.buis.service.ITeamMemberStudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;



@Api(tags = "队伍管理Api")
@RestController
@RequestMapping("/api/team")
public class TeamApiController extends BaseController{

    @Autowired
    private ITeamMemberStudentService teamMemberStudentService;
    @ApiOperation("查询队伍成员学生列表")
    @GetMapping("/listMember/student")
    public TableDataInfo listMemberStudent(TeamMemberStudentQueryDTO teamMemberStudentQueryDTO)
    {
        startPage();
        Long race = teamMemberStudentQueryDTO.getRace();
        Long team = teamMemberStudentQueryDTO.getTeam();
        List<UserInfo> result = teamMemberStudentService.selectTeamMemberStudentQueryList(race,team);
        return getDataTable(result);
    }



    @ApiOperation("查询队伍列表")
    @GetMapping("/listTeam")
    public TableDataInfo listTeams(TeamQueryDTO teamQueryDTO, HttpServletRequest request)
    {
        //判空
        if (teamQueryDTO == null ){
            //TODO 错误状态码待修改
            throw new ServiceException("参数有误!",5000);
        }
        //获取当前登录用户
        LoginUser loginUser = getLoginUser();

    }



}
