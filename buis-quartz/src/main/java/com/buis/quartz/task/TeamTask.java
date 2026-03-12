package com.buis.quartz.task;

import com.alibaba.fastjson2.JSON;
import com.buis.common.core.page.MyPageVo;
import com.buis.common.core.redis.RedisCache;
import com.buis.common.utils.PageUtils;
import com.buis.model.domain.TeamInfo;
import com.buis.service.ITeamInfoService;
import com.buis.model.vo.TeamInfoVo;
import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * 定时任务
 */
@Component("TeamTask")
@Slf4j
public class TeamTask {
    @Autowired
    private ITeamInfoService teamInfoService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 每天凌晨2点运行
     * 队伍信息预热
     */
    public void warmUp() {
        log.info("开始执行队伍数据预热");
        //清理缓存
        String pattern = "team:*";
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            // 删除匹配的键
            redisTemplate.delete(keys);
        }
        TeamInfo teamInfo = new TeamInfo();

        //清理分页缓存
        String pattern1 = "teams:page:*:size:*";
        Set<String> keys1 = redisTemplate.keys(pattern1);
        if (keys1 != null && !keys1.isEmpty()) {
            // 删除匹配的键
            redisTemplate.delete(keys1);
        }
        // 分页查询队伍信息
        //缓存首页的队伍信息
        int[] pageNums = {1};
        int[] pageSizes = {8,10};
        teamInfo.setIsPublish("Y");

        for (int pageNum : pageNums) {
            for (int pageSize : pageSizes) {
                Page<TeamInfoVo> page = PageUtils.startPage(pageNum, pageSize);
                List<TeamInfoVo> teamInfoVos1 = teamInfoService.selectTeamInfoList(teamInfo);
                MyPageVo myPageVo = new MyPageVo(page, teamInfoVos1);
                String pageCacheKey = getPageCacheKey(pageNum, pageSize);
                redisCache.setCacheObject(pageCacheKey, JSON.toJSONString(myPageVo), 1, TimeUnit.DAYS);
            }
        }
        log.info("队伍数据预热完成");
    }

    private String getPageCacheKey(int pageNum, int pageSize) {
        return String.format("teams:page:%d:size:%d", pageNum, pageSize);
    }
}
