package com.buis.quartz.task;

import com.buis.domain.RaceInfo;
import com.buis.enums.RaceStatus;
import com.buis.service.IRaceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定时任务
 */
@Component("RaceTask")
public class RaceTask {
    @Autowired
    private IRaceInfoService raceInfoService;



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


}
