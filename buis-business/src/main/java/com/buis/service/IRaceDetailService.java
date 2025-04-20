package com.buis.service;

import java.util.List;
import com.buis.domain.RaceDetail;
import com.buis.vo.RaceDetailVo;

/**
 * 活动详情Service接口
 * 
 * @author buis
 * @date 2024-10-11
 */
public interface IRaceDetailService 
{
    /**
     * 查询活动详情
     * 
     * @param id 活动详情主键
     * @return 活动详情
     */
    public RaceDetail selectRaceDetailById(Long id);

    /**
     * 查询活动详情列表
     *
     * @param raceDetail 活动详情
     * @return 活动详情集合
     */
    public List<RaceDetailVo> selectRaceDetailList(RaceDetail raceDetail);

    /**
     * 新增活动详情
     * 
     * @param raceDetail 活动详情
     * @return 结果
     */
    public int insertRaceDetail(RaceDetail raceDetail);

    /**
     * 新增第二课堂活动详情
     */
    public int insertSecondRaceDetail(RaceDetail raceDetail);

    /**
     * 修改活动详情
     * 
     * @param raceDetail 活动详情
     * @return 结果
     */
    public int updateRaceDetail(RaceDetail raceDetail);

    /**
     * 批量删除活动详情
     * 
     * @param ids 需要删除的活动详情主键集合
     * @return 结果
     */
    public int deleteRaceDetailByIds(Long[] ids);

    /**
     * 删除活动详情信息
     * 
     * @param id 活动详情主键
     * @return 结果
     */
    public int deleteRaceDetailById(Long id);

    RaceDetail selectRaceDetailByRaceId(Long raceId);

    List<RaceDetail> batchSelectRaceDetailByRaceIds(List<Long> raceIds);

    List<RaceDetailVo> selectRaceDetailVoList(RaceDetailVo raceDetailVo);
}
