package com.buis.service.impl;

import java.util.List;
import com.buis.common.utils.DateUtils;
import com.buis.service.IRaceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.buis.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.buis.domain.Weight;
import com.buis.mapper.RaceDetailMapper;
import com.buis.domain.RaceDetail;
import com.buis.service.IRaceDetailService;

import javax.annotation.Resource;

/**
 * 比赛详情Service业务层处理
 * 
 * @author buis
 * @date 2024-10-11
 */
@Service
public class RaceDetailServiceImpl implements IRaceDetailService 
{
    @Resource
    private RaceDetailMapper raceDetailMapper;

    /**
     * 查询比赛详情
     * 
     * @param id 比赛详情主键
     * @return 比赛详情
     */
    @Override
    public RaceDetail selectRaceDetailById(Long id)
    {
        RaceDetail raceDetailVo = raceDetailMapper.selectRaceDetailById(id);
        return raceDetailVo;
    }

    /**
     * 查询比赛详情列表
     *
     * @param raceDetail 比赛详情
     * @return 比赛详情
     */
    @Override
    public List<RaceDetail> selectRaceDetailList(RaceDetail raceDetail)
    {
        List<RaceDetail> raceDetailVos = raceDetailMapper.selectRaceDetailList(raceDetail);
        return raceDetailVos;
    }

    /**
     * 新增比赛详情
     * 
     * @param raceDetail 比赛详情
     * @return 结果
     */
    @Transactional
    @Override
    public int insertRaceDetail(RaceDetail raceDetail)
    {
        raceDetail.setCreateTime(DateUtils.getNowDate());
        int rows = raceDetailMapper.insertRaceDetail(raceDetail);
        insertWeight(raceDetail);
        return rows;
    }

    /**
     * 修改比赛详情
     * 
     * @param raceDetail 比赛详情
     * @return 结果
     */
    @Transactional
    @Override
    public int updateRaceDetail(RaceDetail raceDetail)
    {
        raceDetail.setUpdateTime(DateUtils.getNowDate());
        raceDetailMapper.deleteWeightByRaceD(raceDetail.getId());
        insertWeight(raceDetail);
        return raceDetailMapper.updateRaceDetail(raceDetail);
    }

    /**
     * 批量删除比赛详情
     * 
     * @param ids 需要删除的比赛详情主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteRaceDetailByIds(Long[] ids)
    {
        raceDetailMapper.deleteWeightByRaceDs(ids);
        return raceDetailMapper.deleteRaceDetailByIds(ids);
    }

    /**
     * 删除比赛详情信息
     * 
     * @param id 比赛详情主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteRaceDetailById(Long id)
    {
        raceDetailMapper.deleteWeightByRaceD(id);
        return raceDetailMapper.deleteRaceDetailById(id);
    }

    /**
     * 新增分数权重信息
     * 
     * @param raceDetail 比赛详情对象
     */
    public void insertWeight(RaceDetail raceDetail)
    {
        List<Weight> weightList = raceDetail.getWeightList();
        Long id = raceDetail.getId();
        if (StringUtils.isNotNull(weightList))
        {
            List<Weight> list = new ArrayList<Weight>();
            for (Weight weight : weightList)
            {
                weight.setRaceD(id);
                list.add(weight);
            }
            if (list.size() > 0)
            {
                raceDetailMapper.batchWeight(list);
            }
        }
    }
}
