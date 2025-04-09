package com.buis.web.controller.api;

import com.buis.common.core.domain.R;
import com.buis.common.core.page.MyPageVo;
import com.buis.common.utils.SecurityUtils;
import com.buis.domain.*;
import com.buis.service.*;
import com.buis.vo.RaceDetailVo;
import com.buis.vo.RaceInfoVo;
import com.buis.vo.RaceSecondVo;
import com.buis.vo.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

import static com.buis.common.utils.PageUtils.startPage;

@Api(tags = "第二课堂Api")
@RestController
@RequestMapping("/api/second")
public class SecondApiController {

    @Autowired
    private IRaceDetailService raceDetailService;
    @Autowired
    private IRacePublishService racePublishService;
    @Autowired
    private IRaceInfoService raceInfoService;
    @Autowired
    private ISecondService secondService;
    @Autowired
    private IRaceSecondService raceSecondService;
    @Autowired
    private IPreSignStudentService preSignStudentService;
    @Autowired
    private IUserInfoService userInfoService;

    /**
     * 分页查询第二课堂活动申报
     */
    @ApiOperation("分页查询第二课堂活动申报信息")
    @PostMapping("/list")
    public MyPageVo<List<RaceInfo>> list(@RequestBody RaceInfo raceInfo) {
        startPage();
        List<RaceInfo> raceInfoList = raceInfoService.selectSecondActivities(raceInfo);
        return new MyPageVo(raceInfoList);
    }

    /**
     * 分页查询第二课堂发布信息(原)
     */
    @ApiOperation("分页查询第二课堂发布信息")
    @PostMapping("/info/list")
    public MyPageVo<List<RaceDetailVo>> publishList(@RequestBody RaceDetailVo raceDetailVo) {
        startPage();
        List<RaceDetailVo> racePublishes = raceDetailService.selectRaceDetailVoList(raceDetailVo);
        return new MyPageVo(racePublishes);
    }

    /**
     * 分页查询第二课堂发布信息
     */
    @ApiOperation("分页查询第二课堂发布信息")
    @PostMapping("/info/list2")
    public MyPageVo<List<RacePublish>> publishList2(@RequestBody RacePublish racePublish) {
        startPage();
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
        Long user = SecurityUtils.getUserId();
        //申报第二课堂活动
        raceInfo.setParentTid(5L);
        raceInfo.setCreateId(user);
        int ok = raceInfoService.insertRaceInfo(raceInfo);
        if (ok <= 0) {
            return R.fail("申报失败");
        }

        //成功插入之后,race_second表对应的活动id
        RaceInfoVo raceInfoVo = raceInfoService.selectRaceInfoList(raceInfo).get(0);
        Long race = raceInfoVo.getId();

        //关联比赛id和secondID
        RaceSecond raceSecond = new RaceSecond();
        raceSecond.setRace(race);
        raceSecond.setSecond(id);
        raceSecond.setReviewStatus(0L); //待审核
        int i = raceSecondService.insertRaceSecond(raceSecond);
        if (i <= 0) {
            return R.fail("申报失败");
        }
        return R.ok(raceInfoVo);
    }

    /**
     * 教师发布第二课堂活动
     */
//    @ApiOperation("教师发布第二课堂活动")
//    @PostMapping("/publish")
//    @Transactional
//    public R<RacePublish> publish(@RequestBody RacePublish racePublish) {
//        //发布的时候只有等审核状态为已通过才能发布, 而且只能发布是自己申报的活动
//        //查看对应的race_info表id
//        Long race = racePublish.getRace();
//
//        //判断申报的活动是否审核通过
//        RaceInfoVo raceInfoVo = raceInfoService.selectRaceInfoById(race);
//        Long reviewStatus = raceInfoVo.getReviewStatus();
//
//        //审核通过才能填写
//        if (reviewStatus == 1) {
//            int i = racePublishService.insertRacePublish(racePublish);
//            if (i <= 0) {
//                return R.fail("发布失败");
//            }
//            return R.ok(racePublish);
//        }
//        return R.fail("请待活动审核通过再填写发布内容~");
//    }

    /**
     * 教师发布第二课堂活动
     */
    @ApiOperation("教师发布第二课堂活动")
    @PostMapping("/publish")
    @Transactional
    public R<RaceDetail> publish(@ApiParam(name = "raceDetail", value = "发布信息", required = true)
                                 @RequestBody RaceDetail raceDetail,
                                 @ApiParam(name = "id", value = "申报的活动id", required = true)
                                 @RequestParam Long secondId) {
        //发布的时候只有等审核状态为已通过才能发布, 而且只能发布是自己申报的活动
        //判断申报的活动是否审核通过
        RaceSecondVo raceSecondVo = new RaceSecondVo();
        raceSecondVo.setSecond(secondId);
        RaceSecondVo res = raceSecondService.selectRaceSecondList(raceSecondVo).get(0);
        Long status = res.getReviewStatus();
        //审核通过才能填写
        if (status == 1) {
            int i = raceDetailService.insertRaceDetail(raceDetail);
            if (i <= 0) {
                return R.fail("发布失败");
            }
            return R.ok(raceDetail);
        }
        return R.fail("请待活动审核通过再填写发布内容~");
    }

    /**
     * 教师端 分页查询关联xx年第二课堂信息
     */
    @ApiOperation("分页查询xx年第二课堂信息(教师端)")
    @PostMapping("/second/list")
    public MyPageVo<List<Second>> list(@RequestBody Second second) {
        startPage();
        List<Second> list = secondService.selectSecondList(second);
        if (list.isEmpty()) {
            return new MyPageVo(Collections.emptyList());
        }
        return new MyPageVo(list);
    }

    /**
     * 学生报名第二课堂活动
     * @param secondId
     * @return
     */
    @ApiOperation("学生报名第二课堂活动")
    @PostMapping("/second/add")
    public R addPreStudent(@ApiParam(name = "secondId", value = "第二课堂活动id", required = true) @PathVariable Long secondId) {
        Long userId = SecurityUtils.getUserId();
        UserInfoVo user = userInfoService.selectUserInfoById(userId);
        PreSignStudent preSignStudent = preSignStudentService.selectByUserIdAndRace(userId, secondId);
        if (preSignStudent != null) {
            return R.fail("您已经报名过了");
        }
        preSignStudent.setStudent(userId);
        preSignStudent.setRace(secondId);
        preSignStudent.setNumber(user.getNumber());
        preSignStudent.setS_class(user.getStuGrade() + user.getMajorName() + user.getStuClass());
        preSignStudent.setPhone(user.getPhone());
        int i = preSignStudentService.insertPreSignStudent(preSignStudent);
        return R.ok(i);
    }


}
