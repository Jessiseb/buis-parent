package com.buis.service.impl;

import com.buis.common.exception.ServiceException;
import com.buis.common.utils.DateUtils;
import com.buis.common.utils.StringUtils;
import com.buis.domain.RaceDetail;
import com.buis.domain.Weight;
import com.buis.dto.UserLoginDto;
import com.buis.mapper.RaceDetailMapper;
import com.buis.service.IRaceDetailService;
import com.buis.service.IRaceInfoService;
import com.buis.system.service.ISysDictDataService;
import com.buis.vo.RaceDetailVo;
import com.buis.vo.RaceInfoVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 比赛详情Service业务层处理
 *
 * @author buis
 * @date 2024-10-11
 */
@Service
public class RaceDetailServiceImpl implements IRaceDetailService {
    @Resource
    private RaceDetailMapper raceDetailMapper;
    @Resource
    private IRaceInfoService raceInfoService;
    @Resource
    private ISysDictDataService sysDictDataService;


    /**
     * 查询比赛详情
     *
     * @param id 比赛详情主键
     * @return 比赛详情
     */
    @Override
    public RaceDetail selectRaceDetailById(Long id) {
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
    public List<RaceDetailVo> selectRaceDetailList(RaceDetail raceDetail) {

        //查出的Vo
        List<RaceDetailVo> raceDetailVos = raceDetailMapper.selectRaceDetailList(raceDetail);


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
    public int insertRaceDetail(RaceDetail raceDetail) {
        RaceInfoVo raceInfoVo = raceInfoService.selectRaceInfoVoById(raceDetail.getRace());

        if (raceInfoVo.getReviewStatus() == 2){
            throw new ServiceException("比赛审核未通过");
        }
        if (raceInfoVo.getParentTid() == 5 && raceInfoVo.getReviewStatus() != 1) {
            throw new ServiceException("比赛审核中，请耐心等待");
        }
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
    public int updateRaceDetail(RaceDetail raceDetail) {
        raceDetail.setUpdateTime(DateUtils.getNowDate());
//        raceDetailMapper.deleteWeightByRaceD(raceDetail.getId());
        Long race = raceDetail.getRace();
        int reviewStatus = raceDetail.getReviewStatus();
        //审核通过之后把对应的比赛信息状态设置为报名中
        if (reviewStatus == 1) {
            RaceInfoVo raceInfoVo = raceInfoService.selectRaceInfoById(race);
            raceInfoVo.setStatus(1L); //报名中
            raceInfoService.updateRaceInfo(raceInfoVo);
        }
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
    public int deleteRaceDetailByIds(Long[] ids) {
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
    public int deleteRaceDetailById(Long id) {
        raceDetailMapper.deleteWeightByRaceD(id);
        return raceDetailMapper.deleteRaceDetailById(id);
    }

    //    查询比赛信息
    @Override
    public RaceDetail selectRaceDetailByRaceId(Long raceId) {
        RaceDetail raceDetail = raceDetailMapper.selectRaceDetailByRaceId(raceId);
        return raceDetail;
    }

    @Override
    public List<RaceDetail> batchSelectRaceDetailByRaceIds(List<Long> raceIds) {
        return raceDetailMapper.batchSelectRaceDetailByRaceIds(raceIds);
    }

    @Override
    public List<RaceDetailVo> selectRaceDetailVoList(RaceDetailVo raceDetailVo) {
        return raceDetailMapper.selectRaceDetailVoList(raceDetailVo);
    }

    /**
     * 新增分数权重信息
     *
     * @param raceDetail 比赛详情对象
     */
    public void insertWeight(RaceDetail raceDetail) {
        List<Weight> weightList = raceDetail.getWeightList();
        Long id = raceDetail.getId();
        if (StringUtils.isNotNull(weightList)) {
            List<Weight> list = new ArrayList<Weight>();
            for (Weight weight : weightList) {
                weight.setRaceD(id);
                list.add(weight);
            }
            if (list.size() > 0) {
                raceDetailMapper.batchWeight(list);
            }
        }
    }
}
