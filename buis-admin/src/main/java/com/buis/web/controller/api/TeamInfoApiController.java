package com.buis.web.controller.api;

import com.buis.common.core.controller.BaseController;
import com.buis.common.core.page.TableDataInfo;
import com.buis.domain.TeamInfo;
import com.buis.dto.TeamQueryDTO;
import com.buis.service.impl.TeamInfoServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 队伍信息ApiController
 *
 * @author zjx
 * @date 2024-10-16
 */
@Api(tags = "队伍信息ApiController")
@RestController
@RequestMapping("/buis/teamInfoApi")
public class TeamInfoApiController extends BaseController {

    @Autowired
    private TeamInfoServiceImpl teamInfoService;

    @ApiOperation("分页查询队伍信息接口")
    @PostMapping("/pageTeam")
    public TableDataInfo pageTeam(TeamQueryDTO teamQueryDTO){
        //开始分页
        startPage();
        //把DTO的字段复制到TeamInfo
        TeamInfo teamInfo = new TeamInfo();
        BeanUtils.copyProperties(teamQueryDTO,teamInfo);
        //根据teamInfo查询队伍信息
        List<TeamInfo> result = teamInfoService.selectTeamInfoList(teamInfo);
        //返回查询结果
        return getDataTable(result);
    }



}
