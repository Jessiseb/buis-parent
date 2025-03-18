package com.buis.service.impl;

import java.util.List;
import com.buis.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.buis.mapper.AwardRecordsMapper;
import com.buis.domain.AwardRecords;
import com.buis.service.IAwardRecordsService;

/**
 * 奖状汇总Service业务层处理
 * 
 * @author zjx
 * @date 2025-03-17
 */
@Service
public class AwardRecordsServiceImpl implements IAwardRecordsService 
{
    @Autowired
    private AwardRecordsMapper awardRecordsMapper;

    /**
     * 查询奖状汇总
     * 
     * @param id 奖状汇总主键
     * @return 奖状汇总
     */
    @Override
    public AwardRecords selectAwardRecordsById(Long id)
    {
        return awardRecordsMapper.selectAwardRecordsById(id);
    }

    /**
     * 查询奖状汇总列表
     * 
     * @param awardRecords 奖状汇总
     * @return 奖状汇总
     */
    @Override
    public List<AwardRecords> selectAwardRecordsList(AwardRecords awardRecords)
    {
        return awardRecordsMapper.selectAwardRecordsList(awardRecords);
    }

    /**
     * 新增奖状汇总
     * 
     * @param awardRecords 奖状汇总
     * @return 结果
     */
    @Override
    public int insertAwardRecords(AwardRecords awardRecords)
    {
        awardRecords.setCreateTime(DateUtils.getNowDate());
        return awardRecordsMapper.insertAwardRecords(awardRecords);
    }

    /**
     * 修改奖状汇总
     * 
     * @param awardRecords 奖状汇总
     * @return 结果
     */
    @Override
    public int updateAwardRecords(AwardRecords awardRecords)
    {
        awardRecords.setUpdateTime(DateUtils.getNowDate());
        return awardRecordsMapper.updateAwardRecords(awardRecords);
    }

    /**
     * 批量删除奖状汇总
     * 
     * @param ids 需要删除的奖状汇总主键
     * @return 结果
     */
    @Override
    public int deleteAwardRecordsByIds(Long[] ids)
    {
        return awardRecordsMapper.deleteAwardRecordsByIds(ids);
    }

    /**
     * 删除奖状汇总信息
     * 
     * @param id 奖状汇总主键
     * @return 结果
     */
    @Override
    public int deleteAwardRecordsById(Long id)
    {
        return awardRecordsMapper.deleteAwardRecordsById(id);
    }
}
