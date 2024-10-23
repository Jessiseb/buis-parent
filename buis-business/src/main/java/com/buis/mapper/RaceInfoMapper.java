package com.buis.mapper;

import java.util.List;


import com.buis.domain.RaceDetail;
import com.buis.domain.RaceInfo;
import com.buis.domain.Reward;

/**
 * 比赛信息Mapper接口
 * 
 * @author kaven
 * @date 2024-10-23
 */
public interface RaceInfoMapper
{
    /**
     * 查询比赛信息
     * 
     * @param id 比赛信息主键
     * @return 比赛信息
     */
    public RaceInfo selectRaceInfoById(Long id);

    /**
     * 查询比赛信息列表
     * 
     * @param raceInfo 比赛信息
     * @return 比赛信息集合
     */
    public List<RaceInfo> selectRaceInfoList(RaceInfo raceInfo);

    /**
     * 新增比赛信息
     * 
     * @param raceInfo 比赛信息
     * @return 结果
     */
    public int insertRaceInfo(RaceInfo raceInfo);

    /**
     * 修改比赛信息
     * 
     * @param raceInfo 比赛信息
     * @return 结果
     */
    public int updateRaceInfo(RaceInfo raceInfo);

    /**
     * 删除比赛信息
     * 
     * @param id 比赛信息主键
     * @return 结果
     */
    public int deleteRaceInfoById(Long id);

    /**
     * 批量删除比赛信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRaceInfoByIds(Long[] ids);

    /**
     * 批量删除奖项
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRewardByRaces(Long[] ids);
    
    /**
     * 批量新增奖项
     * 
     * @param rewardList 奖项列表
     * @return 结果
     */
    public int batchReward(List<Reward> rewardList);
    

    /**
     * 通过比赛信息主键删除奖项信息
     * 
     * @param id 比赛信息ID
     * @return 结果
     */
    public int deleteRewardByRace(Long id);
}
