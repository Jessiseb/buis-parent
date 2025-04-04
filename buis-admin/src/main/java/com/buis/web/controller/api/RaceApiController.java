package com.buis.web.controller.api;

import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.R;
import com.buis.common.core.page.MyPageVo;
import com.buis.domain.RaceInfo;
import com.buis.domain.RaceType;
import com.buis.service.IRaceInfoService;
import com.buis.service.IRaceTypeService;
import com.buis.vo.RaceInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 比赛信息
 */
@Api(tags = "比赛信息Api")
@RestController
@RequestMapping("/api/race")
public class RaceApiController extends BaseController {
    @Autowired
    private IRaceInfoService raceInfoService;
    @Autowired
    private IRaceTypeService raceTypeService;


    /**
     * 根据比赛id查询比赛信息
     * @param RaceId
     * @return
     */
    @ApiOperation("根据比赛id查询比赛信息")
    @GetMapping("/detail/{RaceId}")
    public R<RaceInfoVo> list(@ApiParam(name = "RaceId", value = "比赛id", required = true) @PathVariable("RaceId") Long RaceId) {
        RaceInfoVo raceInfoVo = raceInfoService.selectRaceInfoById(RaceId);
        return R.ok(raceInfoVo);
    }

    /**
     * 带条件分页查询比赛列表
     * @param pageNum
     * @param pageSize
     * @param raceInfo
     * @return
     */
    @ApiOperation("带条件分页查询比赛列表")
    @GetMapping("/raceList")
    public MyPageVo<List<RaceInfoVo>> list(
            @ApiParam(name = "pageNum", value = "查询页数", required = true) @RequestParam Integer pageNum,
            @ApiParam(name = "pageSize", value = "查询数量", required = true) @RequestParam Integer pageSize,
            @ApiParam(name = "raceInfo", value = "比赛信息", required = false) RaceInfo raceInfo) {
        startPage();
        //判断是不是条件查询
        if(raceInfoService.allFieldsEmpty(raceInfo)) {
            //获取缓存
            return raceInfoService.selectRaceInfoListFromRedis(raceInfo);
        }

        List<RaceInfoVo> list = raceInfoService.getRaceInfoList(raceInfo);

        return new MyPageVo(list);
    }

    /**
     * 查询比赛类型列表
     */
    @ApiOperation("查询比赛类型列表")
    @GetMapping("/typeList")
    public R<List<RaceType>> list(@ApiParam(name = "raceType", value = "比赛类型信息", required = false) RaceType raceType) {
        List<RaceType> list = raceTypeService.selectRaceTypeList(raceType);
        return R.ok(list);
    }

    /**
     * 查询用户参加的所有比赛
     */
    @ApiOperation("查询用户参加的所有比赛")
    @GetMapping("/getUserRaceList")
    public R<List<RaceInfo>> getUserRaceList(@ApiParam(name = "userId", value = "用户id", required = true)
                                                         @RequestParam Long userId) {
        //根据用户id查出用户所参加的比赛
        List<RaceInfo> result = raceInfoService.getUserRaceList(userId);
        return R.ok(result);
    }

}
