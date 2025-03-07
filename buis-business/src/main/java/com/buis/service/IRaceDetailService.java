package com.buis.service;

import java.util.List;
import com.buis.domain.RaceDetail;
import com.buis.vo.RaceDetailVo;

/**
 * 比赛详情Service接口
 * 
 * @author buis
 * @date 2024-10-11
 */
public interface IRaceDetailService 
{
    /**
     * 查询比赛详情
     * 
     * @param id 比赛详情主键
     * @return 比赛详情
     */
    public RaceDetail selectRaceDetailById(Long id);

    /**
     * 查询比赛详情列表
     *
     * @param raceDetail 比赛详情
     * @return 比赛详情集合
     */
    public List<RaceDetailVo> selectRaceDetailList(RaceDetail raceDetail);

    /**
     * 新增比赛详情
     * 
     * @param raceDetail 比赛详情
     * @return 结果
     */
    public int insertRaceDetail(RaceDetail raceDetail);

    /**
     * 修改比赛详情
     * 
     * @param raceDetail 比赛详情
     * @return 结果
     */
    public int updateRaceDetail(RaceDetail raceDetail);

    /**
     * 批量删除比赛详情
     * 
     * @param ids 需要删除的比赛详情主键集合
     * @return 结果
     */
    public int deleteRaceDetailByIds(Long[] ids);

    /**
     * 删除比赛详情信息
     * 
     * @param id 比赛详情主键
     * @return 结果
     */
    public int deleteRaceDetailById(Long id);

    RaceDetail selectRaceDetailByRaceId(Long raceId);
}
