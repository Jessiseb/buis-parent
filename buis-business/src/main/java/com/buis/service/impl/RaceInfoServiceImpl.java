package com.buis.service.impl;

import java.util.List;
import com.buis.common.utils.DateUtils;
import com.buis.vo.RaceInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.buis.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.buis.domain.Reward;
import com.buis.mapper.RaceInfoMapper;
import com.buis.domain.RaceInfo;
import com.buis.service.IRaceInfoService;

/**
 * 比赛信息Service业务层处理
 * 
 * @author kaven
 * @date 2024-10-23
 */
@Service
public class RaceInfoServiceImpl implements IRaceInfoService 
{
    @Autowired
    private RaceInfoMapper raceInfoMapper;

    /**
     * 查询比赛信息
     * 
     * @param id 比赛信息主键
     * @return 比赛信息
     */
    @Override
    public RaceInfoVo selectRaceInfoById(Long id)
    {
        return raceInfoMapper.selectRaceInfoById(id);
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
