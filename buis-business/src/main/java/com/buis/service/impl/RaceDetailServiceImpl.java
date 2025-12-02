package com.buis.service.impl;

import com.buis.common.exception.ServiceException;
import com.buis.common.utils.DateUtils;
import com.buis.common.utils.StringUtils;
import com.buis.model.domain.RaceDetail;
import com.buis.model.domain.Weight;
import com.buis.model.enums.RaceStatus;
import com.buis.model.enums.ReviewStatus;
import com.buis.mapper.RaceDetailMapper;
import com.buis.mapper.RaceInfoMapper;
import com.buis.service.IRaceDetailService;
import com.buis.service.IRaceInfoService;
import com.buis.system.service.ISysDictDataService;
import com.buis.model.vo.RaceDetailVo;
import com.buis.model.vo.RaceInfoVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    @Resource
    private RaceInfoMapper raceInfoMapper;


    /**
     * 查询比赛详情
     *
     * @param id 比赛详情主键
     * @return 比赛详情
     */
    @Override
    public RaceDetail selectRaceDetailById(Long id) {
        return raceDetailMapper.selectRaceDetailById(id);

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

        if (Objects.equals(raceInfoVo.getReviewStatus(), ReviewStatus.REFUSE.getCode())) {
            throw new ServiceException("比赛审核未通过");
        }
        if (raceInfoVo.getParentTid() == 5 && !Objects.equals(raceInfoVo.getReviewStatus(), ReviewStatus.PASS.getCode())) {
            throw new ServiceException("比赛审核中，请耐心等待");
        }
        raceDetail.setCreateTime(DateUtils.getNowDate());
        raceDetail.setReviewStatus(ReviewStatus.PUBLISH.getCode());
        raceInfoVo.setReviewStatus(ReviewStatus.PUBLISH.getCode());
        raceInfoMapper.updateRaceInfo(raceInfoVo);
        return raceDetailMapper.insertRaceDetail(raceDetail);

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
        long reviewStatus = raceDetail.getReviewStatus();
        //审核通过之后把对应的比赛信息状态设置为报名中
        if (reviewStatus == ReviewStatus.PUBLISH.getCode()) {
            RaceInfoVo raceInfoVo = raceInfoService.selectRaceInfoById(race);
            raceInfoVo.setStatus(RaceStatus.JOIN.getCode()); //报名中
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

    /**
     * 根据活动id查询活动申报信息(比赛信息)
     */
    @Override
    public RaceDetailVo selectRaceDetailVoByRaceId(Long raceId) {
        return raceDetailMapper.selectRaceDetailVoByRaceId(raceId);

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
