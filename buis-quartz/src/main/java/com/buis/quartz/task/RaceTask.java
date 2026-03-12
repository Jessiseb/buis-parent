package com.buis.quartz.task;

import com.buis.common.core.page.MyPageVo;
import com.buis.common.core.redis.RedisCache;
import com.buis.common.utils.PageUtils;
import com.buis.model.domain.RaceInfo;
import com.buis.model.enums.RaceStatus;
import com.buis.mapper.RaceInfoMapper;
import com.buis.service.IRaceInfoService;
import com.buis.model.vo.HotRaceVo;
import com.buis.model.vo.RaceInfoVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务
 */
@Component("RaceTask")
public class RaceTask {
    @Autowired
    private IRaceInfoService raceInfoService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RaceInfoMapper raceInfoMapper;



    /**
     * 每天1点10分运行
     * 检查未开始的比赛并与当前时间对比来改变比赛状态
     */
//    @Scheduled(cron = "0 10 1 * * ?")
    public void checkRaceStatusPREPARE() {
        System.out.println("执行定时任务===检查未开始的比赛状态");
        //找出状态为未开始的比赛
        List<RaceInfo> raceInfos = raceInfoService.selectRaceInfoByStatu(RaceStatus.PREPARE.getCode());
        //循环判断比赛的开始时间是否超过当前时间
        for (RaceInfo raceInfo : raceInfos) {
            //超过了则把状态修改为已开始
            if (raceInfo.getRaceStart().getTime() < System.currentTimeMillis()) {
                raceInfo.setStatus(RaceStatus.JOIN.getCode());//设置为报名中
                raceInfoService.updateRaceInfo(raceInfo);
            }
        }
    }

    /**
     * 每天0点0分运行
     * 检查报名钟的比赛并与比赛截止时间对比来改变比赛状态
     */
//    @Scheduled(cron = "0 0 0 * * ?")
    public void checkRaceStatusJOIN() {
        System.out.println("执行定时任务===检查报名中的比赛状态");
        //找出状态为未开始的比赛
        List<RaceInfo> raceInfos = raceInfoService.selectRaceInfoByStatu(RaceStatus.JOIN.getCode());
        //循环判断比赛的开始时间是否超过当前时间
        for (RaceInfo raceInfo : raceInfos) {
            //超过了比赛截止时间则把状态修改为已开始
            if (raceInfo.getJoinEnd().getTime() < System.currentTimeMillis()) {
                raceInfo.setStatus(RaceStatus.CHECK.getCode());//设置为审核中
                raceInfoService.updateRaceInfo(raceInfo);
            }
        }
    }

    /**
     * 缓存首页的比赛信息
     * 每天2点0分运行
     * @throws JsonProcessingException
     */
    public void warmUp() throws JsonProcessingException {
        System.out.println("执行定时任务===缓存预热分页比赛信息");
        //清理缓存
        String pattern = "races:page:*:size:*";
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            // 删除匹配的键
            redisTemplate.delete(keys);
        }
        //删除所有是否报名缓存
        String pattern1 = "isJoin:race:*:user:*";
        Set<String> keys1 = redisTemplate.keys(pattern1);
        if (keys1 != null && !keys1.isEmpty()) {
            // 删除匹配的键
            redisTemplate.delete(keys1);
        }

        //删除所有是否预报名缓存
        String pattern2 = "isPreJoin:race:*:user:*";
        Set<String> keys2 = redisTemplate.keys(pattern2);
        if (keys2 != null && !keys2.isEmpty()) {
            redisTemplate.delete(keys2);
        }
        //分页查询比赛信息
        //缓存首页的比赛信息
        int pageNums = 1;
        int pageSizes = 10;
        RaceInfo raceInfo = new RaceInfo();
        Page<RaceInfoVo> page = PageUtils.startPage(pageNums, pageSizes);
        List<RaceInfoVo> raceInfoVos = raceInfoMapper.selectRaceInfoList(raceInfo);
        MyPageVo myPageVo = new MyPageVo(page, raceInfoVos);
        String pageCacheKey = getPageCacheKey(pageNums, pageSizes);
        ObjectMapper objectMapper = new ObjectMapper();
        redisCache.setCacheObject(pageCacheKey, objectMapper.writeValueAsString(myPageVo), 1, TimeUnit.DAYS);
        System.out.println("缓存预热完成");
    }

    private String getPageCacheKey(int pageNum, int pageSize) {
        return String.format("races:page:%d:size:%d", pageNum, pageSize);
    }

    /**
     * 缓存热门比赛
     * 每天0点00分运行
     * @throws JsonProcessingException
     */
    public void warmUpHotRace() throws JsonProcessingException {
        System.out.println("执行定时任务===缓存预热热门比赛");
        //清理缓存
        redisCache.deleteObject("hotRace");

        //缓存热门比赛
        List<HotRaceVo> hotRaceVoList = raceInfoService.getHotRace();
        ObjectMapper objectMapper = new ObjectMapper();
        if (CollectionUtils.isNotEmpty(hotRaceVoList)) {
            redisCache.setCacheObject("hotRace", objectMapper.writeValueAsString(hotRaceVoList), 1, TimeUnit.DAYS);
        }
        System.out.println("缓存预热完成");
    }


}
