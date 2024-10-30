package com.buis.web.controller.api;

import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.R;
import com.buis.common.core.page.TableDataInfo;
import com.buis.domain.RaceInfo;
import com.buis.service.IRaceInfoService;
import com.buis.service.ITeamMemberTeacherService;
import com.buis.service.IUserInfoService;
import com.buis.vo.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
public class RaceApiController extends BaseController {
    @Autowired
    private IRaceInfoService raceInfoService;
    @Resource
    private ITeamMemberTeacherService teamMemberTeacherService;
    @Resource
    private IUserInfoService userInfoService;

    @ApiOperation("根据比赛id查询指导老师信息")
    @PostMapping("/listTeachers/{RaceId}")
    public TableDataInfo listTeachersByRaceId(@ApiParam(name = "RaceId", value = "比赛id", required = true)@PathVariable("RaceId") Long RaceId){
        //开始分页
        startPage();

        //根据比赛id查出队伍老师表中的老师id
        List<Integer> teacherIds = teamMemberTeacherService.listTeacherIdByRaceId(RaceId);

        //去重
        List<Integer> Ids = teacherIds.stream().distinct().collect(Collectors.toList());

        //根据老师ids查出所有老师的信息
        List<UserInfoVo> userInfos = teamMemberTeacherService.listTeachersByIds(Ids);

        //返回结果
        return getDataTable(userInfos);
    }



    @ApiOperation("查询比赛信息列表")
    @GetMapping("/list")
    public TableDataInfo list(@ApiParam(name = "raceInfo", value = "比赛信息", required = false)RaceInfo raceInfo) {
        startPage();
        List<RaceInfo> list = raceInfoService.selectRaceInfoList(raceInfo);
        return getDataTable(list);
    }


}
