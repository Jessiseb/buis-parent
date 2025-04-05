package com.buis.web.controller.api;

import com.buis.common.core.domain.R;
import com.buis.common.core.page.MyPageVo;
import com.buis.domain.RaceInfo;
import com.buis.domain.RacePublish;
import com.buis.domain.RaceSecond;
import com.buis.service.IRaceDeclareService;
import com.buis.service.IRaceInfoService;
import com.buis.service.IRacePublishService;
import com.buis.service.IRaceSecondService;
import com.buis.vo.RaceInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "第二课堂Api")
@RestController
@RequestMapping("/api/second")
public class SecondApiController {

    @Autowired
    private IRaceDeclareService raceDeclareService;
    @Autowired
    private IRacePublishService racePublishService;
    @Autowired
    private IRaceInfoService raceInfoService;
    @Autowired
    private IRaceSecondService raceSecondService;

    /**
     * 分页查询第二课堂活动申报
     */
    @ApiOperation("分页查询第二课堂活动申报")
    @PostMapping("/list")
    public MyPageVo<List<RaceInfo>> list(@RequestBody RaceInfo raceInfo) {
        List<RaceInfo> raceInfoList = raceInfoService.selectRaceSecondActivities(raceInfo);
        return new MyPageVo(raceInfoList);
    }

    /**
     * 分页查看第二课堂申报信息
     */
    @ApiOperation("分页查看第二课堂信息")
    @PostMapping("/publish/list")
    public MyPageVo<List<RacePublish>> publishList(@RequestBody RacePublish racePublish) {
        List<RacePublish> racePublishes = racePublishService.selectRacePublishList(racePublish);
        return new MyPageVo(racePublishes);
    }


    /**
     * 教师申报第二课堂活动
     */
    @ApiOperation("教师申报第二课堂活动")
    @PostMapping("/declare")
    @Transactional
    public R<RaceInfoVo> declare(@ApiParam(name = "raceInfo", value = "申报信息", required = true)
                                  @RequestBody RaceInfo raceInfo,
                                  @ApiParam(name = "id", value = "关联第二课堂表id", required = true)
                                  @RequestParam Long id) {
        //申报第二课堂活动
//        RaceInfo raceInfo = new RaceInfo();
//        BeanUtils.copyProperties(raceDeclare, raceInfo);
        raceInfo.setReviewStatus(0L); //待审核
        int ok = raceInfoService.insertRaceInfo(raceInfo);
        if (ok <= 0) {
            return R.fail("申报失败");
        }
        //成功插入之后,race_second表对应的活动id
        RaceInfoVo raceInfoVo = raceInfoService.selectRaceInfoList(raceInfo).get(0);
        Long race = raceInfoVo.getId();

        //关联比赛id
        RaceSecond raceSecond = raceSecondService.selectRaceSecondById(id);
        raceSecond.setRace(race);
        int i = raceSecondService.updateRaceSecond(raceSecond);
        if (i <= 0) {
            return R.fail("申报失败");
        }
        return R.ok(raceInfoVo);
    }

    /**
     * 教师发布第二课堂活动
     */
    @ApiOperation("教师发布第二课堂活动")
    @PostMapping("/publish")
    @Transactional
    public R<RacePublish> publish(@RequestBody RacePublish racePublish) {
        //查看对应的race_info表id
        Long race = racePublish.getRace();

        //判断申报的活动是否审核通过
        RaceInfoVo raceInfoVo = raceInfoService.selectRaceInfoById(race);
        Long reviewStatus = raceInfoVo.getReviewStatus();

        //审核通过才能填写
        if (reviewStatus == 1) {
            int i = racePublishService.insertRacePublish(racePublish);
            if (i <= 0) {
                return R.fail("发布失败");
            }
            return R.ok(racePublish);
        }
        return R.fail("请待活动审核通过再填写发布内容~");
    }

}
