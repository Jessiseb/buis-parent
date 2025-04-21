package com.buis.mapper;

import java.util.List;


import com.buis.domain.RaceDetail;
import com.buis.domain.RaceInfo;
import com.buis.domain.Reward;
import com.buis.dto.GradeStatistic;
import com.buis.vo.RaceInfoVo;
import com.buis.vo.SecondClassCountVo;
import org.apache.ibatis.annotations.Param;

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
    public RaceInfoVo selectRaceInfoById(@Param("id") Long id);

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

//    void updateRaceInfoStatus(@Param("id") Long id,@Param("status") Integer status);

    //根据状态查询比赛信息
    public List<RaceInfo> selectRaceInfoByStatu(Long i);

    //根据比赛信息ids查询比赛信息列表
    List<RaceInfo> selectRaceInfoByRaceIds(List<Long> raceIds);

    List<SecondClassCountVo> getSecondClassCountByMonth(Integer month);

    List<SecondClassCountVo> getSecondClassCountByYear(Integer year);

    //根据二级类型id查询一级类型id
    Long selectRaceTypeBySecondTypeId(@Param("id")Long id);

    //查询所有比赛
    List<RaceInfoVo> selectRaceInfoAll();


    List<RaceInfoVo> getRaceInfoList(RaceInfo raceInfo);

    List<RaceInfo> selectSecondActivities(RaceInfo raceInfo);

    List<GradeStatistic> getStudentGradeCount();

    String getTeacherInstructCountFromSecond(Long userId);

    RaceInfo selectRaceInfoByName(@Param("name") String name);

}
