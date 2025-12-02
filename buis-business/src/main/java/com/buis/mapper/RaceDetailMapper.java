package com.buis.mapper;

import java.util.List;


import com.buis.model.domain.RaceDetail;
import com.buis.model.domain.Weight;
import com.buis.model.vo.RaceDetailVo;
import org.apache.ibatis.annotations.Param;

/**
 * 比赛详情Mapper接口
 * 
 * @author buis
 * @date 2024-10-11
 */
public interface RaceDetailMapper
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
    public List<RaceDetailVo> selectRaceDetailList(@Param("raceDetail") RaceDetail raceDetail);

    /**
     * 查询比赛详情列表
     * @param raceDetailVo
     * @return
     */
    public List<RaceDetailVo> selectRaceDetailVoList(@Param("raceDetailVo") RaceDetailVo raceDetailVo);

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
     * 删除比赛详情
     * 
     * @param id 比赛详情主键
     * @return 结果
     */
    public int deleteRaceDetailById(Long id);

    /**
     * 批量删除比赛详情
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRaceDetailByIds(Long[] ids);

    /**
     * 批量删除分数权重
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWeightByRaceDs(Long[] ids);
    
    /**
     * 批量新增分数权重
     * 
     * @param weightList 分数权重列表
     * @return 结果
     */
    public int batchWeight(List<Weight> weightList);
    

    /**
     * 通过比赛详情主键删除分数权重信息
     * 
     * @param id 比赛详情ID
     * @return 结果
     */
    public int deleteWeightByRaceD(Long id);

    RaceDetailVo selectRaceDetailVoByRaceId(@Param("raceId") Long raceId);

    List<RaceDetail> batchSelectRaceDetailByRaceIds(List<Long> raceIds);
}
