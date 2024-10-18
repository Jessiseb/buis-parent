package com.buis.web.controller.api;

import com.buis.common.core.controller.BaseController;
import com.buis.common.core.page.TableDataInfo;
import com.buis.domain.TeamMemberTeacher;
import com.buis.dto.TeamTeacherQueryDTO;
import com.buis.service.impl.TeamMemberTeacherServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "队伍老师ApiController")
@RestController
@RequestMapping("/buis/teamTeacherApi")
public class TeamMemberTeacherApiController extends BaseController {

    @Autowired
    private TeamMemberTeacherServiceImpl teamMemberTeacherService;

    @ApiOperation("分页查询老师信息列表")
    @PostMapping("/pageTeacher")
    public TableDataInfo pageTeacher(TeamTeacherQueryDTO teamTeacherQueryDTO){
        //开始分页
        startPage();
        TeamMemberTeacher teamMemberTeacher = new TeamMemberTeacher();
        BeanUtils.copyProperties(teamTeacherQueryDTO,teamMemberTeacher);
        List<TeamMemberTeacher> result = teamMemberTeacherService.selectTeamMemberTeacherList(teamMemberTeacher);
        return getDataTable(result);
    }

}
