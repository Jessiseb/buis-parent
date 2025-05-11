package com.buis.web.controller.api;

import cn.dev33.satoken.stp.StpUtil;
import com.buis.common.core.domain.R;
import com.buis.common.core.page.MyPageVo;
import com.buis.common.exception.ServiceException;
import com.buis.common.utils.SecurityUtils;
import com.buis.common.utils.bean.BeanUtils;
import com.buis.domain.*;
import com.buis.enums.RaceLevel;
import com.buis.enums.RaceStatus;
import com.buis.enums.ReviewStatus;
import com.buis.service.*;
import com.buis.vo.RaceDetailVo;
import com.buis.vo.RaceInfoVo;
import com.buis.vo.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
    private IAssistantService assistantService;
    @Autowired
    private IUserInfoService userInfoService;



    /**
     * 分页查询第二课堂活动申报
     *
     * @return
     */
    @ApiOperation("分页查询第二课堂活动申报信息")
    @PostMapping("/list")
    public MyPageVo list(@RequestBody RaceInfo raceInfo,
                         @ApiParam(name = "pageNum", value = "查询页数", required = true) @RequestParam Integer pageNum,
                         @ApiParam(name = "pageSize", value = "查询数量", required = true) @RequestParam Integer pageSize) {
        startPage();
        List<RaceInfoVo> raceInfoList = raceInfoService.selectSecondActivities(raceInfo);
        return new MyPageVo(raceInfoList);
    }

    /**
     * 分页查询我申报的第二课堂活动
     */
    @ApiOperation("分页查询我申报的活动(教师端)")
    @PostMapping("/getMyDeclare")
    public MyPageVo<List<RaceInfoVo>> getMyDeclare(@ApiParam(name = "pageNum", value = "查询页数", required = true) @RequestParam Integer pageNum,
                                                   @ApiParam(name = "pageSize", value = "查询数量", required = true) @RequestParam Integer pageSize) {
        startPage();
        //获取登录用户
        UserInfo loginUser = userInfoService.getLoginUser();
        List<RaceInfoVo> raceInfoList = raceInfoService.selectMyDeclare(loginUser.getId());
        return new MyPageVo(raceInfoList);
    }


    /**
     * 分页查询第二课堂发布信息(原)
     */
    @ApiOperation("分页查询第二课堂发布信息")
    @PostMapping("/info/list")
    public MyPageVo<List<RaceDetailVo>> publishList(@RequestBody RaceDetailVo raceDetailVo,
                                                    @ApiParam(name = "pageNum", value = "查询页数", required = true) @RequestParam Integer pageNum,
                                                    @ApiParam(name = "pageSize", value = "查询数量", required = true) @RequestParam Integer pageSize) {
        startPage();
        List<RaceDetailVo> racePublishes = raceDetailService.selectRaceDetailVoList(raceDetailVo);
        return new MyPageVo(racePublishes);
    }

    /**
     * 分页查询第二课堂发布信息
     */
    @ApiOperation("分页查询第二课堂发布信息")
    @PostMapping("/info/list2")
    public MyPageVo<List<RacePublish>> publishList2(@RequestBody RacePublish racePublish,
                                                    @ApiParam(name = "pageNum", value = "查询页数", required = true) @RequestParam Integer pageNum,
                                                    @ApiParam(name = "pageSize", value = "查询数量", required = true) @RequestParam Integer pageSize) {
        startPage();
        List<RacePublish> racePublishes = racePublishService.selectRacePublishList(racePublish);
        return new MyPageVo(racePublishes);
    }


    /**
     * 教师申报第二课堂活动
     */
    @ApiOperation("教师申报第二课堂活动")
    @PostMapping("/declare")
    @Transactional(rollbackFor = Exception.class)
    public R<RaceInfoVo> declare(@ApiParam(name = "raceInfo", value = "申报信息", required = true)
                                  @RequestBody RaceInfo raceInfo,
                                  @ApiParam(name = "id", value = "关联第二课堂表id", required = true)
                                  @RequestParam Long id) {
        if (raceInfo == null || id == null) {
            return R.fail("参数不能为空");
        }
        String user = StpUtil.getLoginId() + "";
        //申报第二课堂活动
        raceInfo.setParentTid(5L);
        raceInfo.setCreateId(Long.valueOf(user));
        raceInfo.setReviewStatus(ReviewStatus.PREPARE.getCode());
        //默认比赛级别为校级
        raceInfo.setLevel(RaceLevel.UNIVERSITY_LEVEL.getCode());
        //插入前判断该数据是否存在  存在则为更新操作
        RaceInfoVo raceInfoVo1 = raceInfoService.selectRaceInfoById(raceInfo.getId());
        int ok;
        boolean isUpdate;
        isUpdate = raceInfoVo1 != null;

        //判断是更新还是插入操作
        if (isUpdate) {
            //更新
            ok = raceInfoService.updateRaceInfo(raceInfo);
        } else {
            //插入
            ok = raceInfoService.insertRaceInfo(raceInfo);
        }
        if (ok <= 0) {
            return R.fail("申报失败");
        }
        Long raceId = raceInfo.getId();
        int  ok1;
        if (isUpdate) {
            //查出对应的RaceSecond
            RaceSecond result =  raceSecondService.selectRaceSecondByRace(raceId);
            result.setReviewStatus(ReviewStatus.PREPARE.getCode());
            ok1 = raceSecondService.updateRaceSecond(result);
        } else {
            // 构造关联对象
            RaceSecond raceSecond = new RaceSecond();
            raceSecond.setRace(raceId);
            raceSecond.setSecond(id);
            ok1 = raceSecondService.insertRaceSecond(raceSecond);
        }
        if (ok1 <= 0) {
            return R.fail("申报失败");
        }

        RaceInfoVo raceInfoVo = new RaceInfoVo();
        BeanUtils.copyProperties(raceInfoVo, raceInfo);
        raceInfoVo.setId(raceId);

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
                                 @ApiParam(name = "raceId", value = "申报的活动id", required = true)
                                 @RequestParam Long raceId) {
        //发布的时候只有等审核状态为已通过才能发布, 而且只能发布是自己申报的活动

        //判断申报的活动是否审核通过
        RaceInfoVo res = raceInfoService.selectRaceInfoVoById(raceId);
        if (res == null) {
            return R.fail("请先申报活动再填写发布内容~");
        }

        Long status = res.getReviewStatus();
        //发布过不能再次发布
        if(Objects.equals(status, ReviewStatus.PUBLISH.getCode())){
            return R.fail("请勿重复发布");
        }

        //不能发布别人申报的活动
        Long createId = res.getCreateId();
        Long userId = Long.valueOf(StpUtil.getLoginId() + "");
        if (!createId.equals(userId)){
            throw new ServiceException("温馨提示：不能发布他人的活动~");
        }

        //审核通过才能填写
        if (Objects.equals(status, ReviewStatus.PASS.getCode())) {
            raceDetail.setRace(raceId);
            int i = raceDetailService.insertRaceDetail(raceDetail);
            if (i <= 0) {
                return R.fail("发布失败");
            }
            //成功发布之后，把race_second表的状态也设置为已发布
            RaceSecond raceSecond = raceSecondService.selectRaceSecondByRace(raceId);
            raceSecond.setReviewStatus(ReviewStatus.PUBLISH.getCode());
            int ok = raceSecondService.updateRaceSecond(raceSecond);
            if (ok <= 0) {
                return R.fail("发布失败,请重试");
            }
            //成功发布之后，设置状态为报名中
            //并且同步race_info一样的报名之间
            res.setJoinStart(raceDetail.getJoinStart());
            res.setJoinEnd(raceDetail.getJoinEnd());
            RaceInfo raceInfo = new RaceInfo();
            BeanUtils.copyBeanProp(res,raceInfo);
            raceInfo.setStatus(RaceStatus.JOIN.getCode());
            raceInfo.setJoinStart(raceDetail.getJoinStart());
            raceInfo.setJoinEnd(raceDetail.getJoinEnd());
            raceInfoService.updateRaceInfo(raceInfo);
            //删除缓存
            raceInfoService.deleteRacesInfoCache();
            return R.ok(raceDetail);
        }

        return R.fail("请待活动审核通过再填写发布内容~");
    }

    /**
     * 教师端 分页查询关联xx年第二课堂信息
     */
    @ApiOperation("分页查询xx年第二课堂信息(教师端)")
    @PostMapping("/second/list")
    public MyPageVo<List<Second>> list(@RequestBody Second second,
                                       @ApiParam(name = "pageNum", value = "查询页数", required = true) @RequestParam Integer pageNum,
                                       @ApiParam(name = "pageSize", value = "查询数量", required = true) @RequestParam Integer pageSize) {
        startPage();
        List<Second> list = secondService.selectSecondList(second);
        if (list.isEmpty()) {
            return new MyPageVo(Collections.emptyList());
        }
        return new MyPageVo(list);
    }

    /**
     * 学生报名第二课堂
     */
    @ApiOperation("学生报名第二课堂")
    @PostMapping("/student/sign")
    public R<Integer> studentSign(@ApiParam(name = "id", value = "第二课堂活动id", required = true)
                                  @RequestParam(value = "id") Long id,
                                  @ApiParam(name = "preSignStudent", value = "第二课堂活动报名表", required = true)
                                  @RequestBody PreSignStudent preSignStudent) {
        //登录用户
        Long userId = SecurityUtils.getFrontLoginUserId();
        return R.ok(preSignStudentService.studentSign(id, userId, preSignStudent));
    }

    /**
     * 根据活动id查询学生助理
     */
    @ApiOperation("根据活动id查询学生助理")
    @GetMapping("/list")
    public R<List<UserInfoVo>> list(@ApiParam(name = "id", value = "第二课堂活动id", required = true)
                                    @RequestParam(value = "id")Long id) {
        List<UserInfoVo> list = assistantService.selectAssistantListByRace(id);
        return R.ok(list);
    }


    /**
     * 根据活动id查询活动发布信息
     */
    @ApiOperation("根据活动id查询活动发布信息")
    @PostMapping("/info/getRaceDetailByRaceId")
    public R<RaceDetail> getRaceDetailByRaceId(@ApiParam(name = "RaceId", value = "申报活动id", required = true)
                              @RequestParam(value = "RaceId")Long RaceId) {
        RaceDetail raceDetail = raceDetailService.selectRaceDetailByRaceId(RaceId);
        return R.ok(raceDetail);
    }

    /**
     * 根据id查询活动申报信息
     */
    @ApiOperation("根据id查询活动申报信息")
    @PostMapping("/info/getRaceInfoById")
    public R<RaceInfoVo> getRaceInfoById(@ApiParam(name = "id", value = "活动id", required = true)
                                         @RequestParam(value = "id")Long id) {
        RaceInfoVo raceInfoVo = raceInfoService.selectRaceInfoById(id);
        return R.ok(raceInfoVo);
    }



}
