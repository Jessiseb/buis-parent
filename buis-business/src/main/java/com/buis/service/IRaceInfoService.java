package com.buis.service;

import java.util.List;
import com.buis.domain.RaceInfo;
import com.buis.vo.RaceInfoVo;

/**
 * 比赛信息Service接口
 * 
 * @author kaven
 * @date 2024-10-23
 */
public interface IRaceInfoService 
{
    /**
     * 查询比赛信息
     * 
     * @param id 比赛信息主键
     * @return 比赛信息
     */
    public RaceInfoVo selectRaceInfoById(Long id);

    /**
     * 查询比赛信息列表
     * 
     * @param raceInfo 比赛信息
     * @return 比赛信息集合
     */
    public List<RaceInfoVo> selectRaceInfoList(RaceInfo raceInfo);

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
     * 批量删除比赛信息
     * 
     * @param ids 需要删除的比赛信息主键集合
     * @return 结果
     */
    public int deleteRaceInfoByIds(Long[] ids);

    /**
     * 删除比赛信息信息
     * 
     * @param id 比赛信息主键
     * @return 结果
     */
    public int deleteRaceInfoById(Long id);

    /**
     * 判断比赛是否已结束
     * @param raceId
     * @return
     */
    public boolean checkJoinCondition(Long userId,Long raceId);
    /**
     * 检查是否超过了比赛的指导老师数量限制
     * @param raceId       比赛的ID
     * @param teacherId    老师的ID
     * @return
     */
    public boolean checkGuideLimit(Long raceId,Long teacherId);
}
