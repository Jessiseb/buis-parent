package com.buis.web.controller.api;

import com.buis.service.IRaceInfoService;
import com.buis.vo.HotRaceVo;
import com.buis.vo.SecondClassCountVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "统计Api")
@RestController
@RequestMapping("/api/statistics")
public class StatisticsApiController {
    @Autowired
    private IRaceInfoService raceInfoService;


    /**
     * 统计各年级学生的参与活动
     */

    /**
     * 根据月份查询各学院第二课堂活动数量
     * @param month 没有即查询本月
     */
    @ApiOperation("查询本月各学院第二课堂活动数量")
    @GetMapping("/getSecondClassCount/month")
    public List<SecondClassCountVo> getSecondClassCountByMonth(@RequestParam(required = false) Integer month) {
        return raceInfoService.getSecondClassCountByMonth(month);
    }

    /**
     * 根据年份查询各学院第二课堂活动数量
     * @param year 没有即查询本年
     */
    @ApiOperation("查询今年各学院第二课堂活动数量")
    @GetMapping("/getSecondClassCount/year")
    public List<SecondClassCountVo> getSecondClassCountByYear(@RequestParam(required = false) Integer year) {
        return raceInfoService.getSecondClassCountByYear(year);
    }

    /**
     * 获取热门比赛TOP10
     * @return
     */
    @ApiOperation("查询热门比赛")
    @GetMapping("/getHotRaces")
    public List<HotRaceVo> getHotRaces() {
        return raceInfoService.getHotRace();
    }
}
