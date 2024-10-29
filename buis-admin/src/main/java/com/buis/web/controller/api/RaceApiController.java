package com.buis.web.controller.api;

import com.buis.common.core.controller.BaseController;
import com.buis.common.core.page.TableDataInfo;
import com.buis.domain.RaceInfo;
import com.buis.service.IRaceInfoService;
import com.buis.web.controller.manager.RaceInfoController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/raceDetail")
@Api(tags = "比赛信息Api")
public class RaceApiController extends BaseController {
    @Autowired
    private IRaceInfoService raceInfoService;

    @ApiOperation("查询比赛信息列表")
    @GetMapping("/list")
    public TableDataInfo list(RaceInfo raceInfo) {
        startPage();
        List<RaceInfo> list = raceInfoService.selectRaceInfoList(raceInfo);
        return getDataTable(list);
    }
}
