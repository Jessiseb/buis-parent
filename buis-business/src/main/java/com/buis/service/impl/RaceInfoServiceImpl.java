package com.buis.service.impl;

import java.util.List;
import com.buis.common.utils.DateUtils;
import com.buis.domain.RaceDetail;
import com.buis.service.*;
import com.buis.vo.RaceInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.buis.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.buis.domain.Reward;
import com.buis.mapper.RaceInfoMapper;
import com.buis.domain.RaceInfo;

import javax.annotation.Resource;

/**
 * 比赛信息Service业务层处理
 * 
 * @author kaven
 * @date 2024-10-23
 */
@Service
public class RaceInfoServiceImpl implements IRaceInfoService 
{
    @Resource
    private RaceInfoMapper raceInfoMapper;
    @Autowired
    private IRaceDetailService raceDetailService;
    @Autowired
    private ITeamInfoService teamInfoService;
    @Autowired
    private ITeamMemberTeacherService teamMemberTeacherService;
    @Autowired
    private ITeamMemberStudentService teamMemberStudentService;
    /**
     * 查询比赛信息
     * 
     * @param id 比赛信息主键
     * @return 比赛信息
     */
    @Override
    public RaceInfoVo selectRaceInfoById(Long id)

    {
        RaceInfoVo raceInfoVo = raceInfoMapper.selectRaceInfoById(id);
        RaceDetail raceDetail = new RaceDetail();
        raceDetail.setRace(raceInfoVo.getId());
        List<RaceDetail> raceDetailVos = raceDetailService.selectRaceDetailList(raceDetail);
        if (raceDetailVos!=null && raceDetailVos.size() > 0) {
            raceInfoVo.setRaceDetail(raceDetailVos.get(0));
        }

        return raceInfoVo;
    }

    /**
     * 查询比赛信息列表
     * 
     * @param raceInfo 比赛信息
     * @return 比赛信息
     */
    @Override
    public List<RaceInfoVo> selectRaceInfoList(RaceInfo raceInfo)
    {
        return raceInfoMapper.selectRaceInfoList(raceInfo);
    }

    /**
     * 新增比赛信息
     * 
     * @param raceInfo 比赛信息
     * @return 结果
     */
    @Transactional
    @Override
    public int insertRaceInfo(RaceInfo raceInfo)
    {
        raceInfo.setCreateTime(DateUtils.getNowDate());
        int rows = raceInfoMapper.insertRaceInfo(raceInfo);
        insertReward(raceInfo);
        return rows;
    }

    /**
     * 修改比赛信息
     * 
     * @param raceInfo 比赛信息
     * @return 结果
     */
    @Transactional
    @Override
    public int updateRaceInfo(RaceInfo raceInfo)
    {
        raceInfo.setUpdateTime(DateUtils.getNowDate());
        raceInfoMapper.deleteRewardByRace(raceInfo.getId());
        insertReward(raceInfo);
        return raceInfoMapper.updateRaceInfo(raceInfo);
    }

    /**
     * 批量删除比赛信息
     * 
     * @param ids 需要删除的比赛信息主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteRaceInfoByIds(Long[] ids)
    {
        raceInfoMapper.deleteRewardByRaces(ids);
        return raceInfoMapper.deleteRaceInfoByIds(ids);
    }

    /**
     * 删除比赛信息信息
     * 
     * @param id 比赛信息主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteRaceInfoById(Long id)
    {
        raceInfoMapper.deleteRewardByRace(id);
        return raceInfoMapper.deleteRaceInfoById(id);
    }

    /**
     * 判断用户是否已报名
     * @param userId
     * @param raceId
     * @return
     */
    @Override
    public int checkJoin(Long userId, Long raceId) {
        if (userId == null || raceId == null) {
            throw new IllegalArgumentException("userId and raceId cannot be null");
        }
        int res = 0;
        // 判断互选表有没有信息
        res = teamMemberTeacherService.checkJoin(userId, raceId, null);
        if (res <= 0) {
            res = teamMemberStudentService.checkJoin(userId, raceId, null);
            if (res <= 0) {
                // 判断队伍有没有信息
                res = teamInfoService.checkJoin(userId, raceId, null);
            }
        }
        return res;
    }
    /**
     * 新增奖项信息
     * 
     * @param raceInfo 比赛信息对象
     */
    public void insertReward(RaceInfo raceInfo)
    {
        List<Reward> rewardList = raceInfo.getRewardList();
        Long id = raceInfo.getId();
        if (StringUtils.isNotNull(rewardList))
        {
            List<Reward> list = new ArrayList<Reward>();
            for (Reward reward : rewardList)
            {
                reward.setRace(id);
                list.add(reward);
            }
            if (list.size() > 0)
            {
                raceInfoMapper.batchReward(list);
            }
        }
    }
}
