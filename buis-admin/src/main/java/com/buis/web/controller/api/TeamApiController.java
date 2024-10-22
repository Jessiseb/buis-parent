package com.buis.web.controller.api;

import com.buis.common.core.page.TableDataInfo;
import com.buis.domain.UserInfo;
import com.buis.dto.TeamMemberStudentQueryDTO;
import com.buis.service.ITeamMemberStudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.buis.common.core.controller.BaseController;


import java.util.List;

import java.util.List;



@Api(tags = "队伍管理Api")
@RestController
@RequestMapping("/api/team")
public class TeamApiController extends BaseController{
    @Autowired
    private ITeamMemberStudentService teamMemberStudentService;
    @ApiOperation("查询队伍成员学生列表")
    @GetMapping("/listMember/student")
    public TableDataInfo list(TeamMemberStudentQueryDTO teamMemberStudentQueryDTO)
    {
        startPage();
        Long race = teamMemberStudentQueryDTO.getRace();
        Long team = teamMemberStudentQueryDTO.getTeam();
        List<UserInfo> result = teamMemberStudentService.selectTeamMemberStudentQueryList(race,team);
        return getDataTable(result);

    }
}
