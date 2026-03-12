package com.buis.mapper;

import java.util.List;
import com.buis.model.domain.AwardRecords;

/**
 * 奖状汇总Mapper接口
 * 
 * @author zjx
 * @date 2025-03-17
 */
public interface AwardRecordsMapper 
{
    /**
     * 查询奖状汇总
     * 
     * @param id 奖状汇总主键
     * @return 奖状汇总
     */
    public AwardRecords selectAwardRecordsById(Long id);

    /**
     * 查询奖状汇总列表
     * 
     * @param awardRecords 奖状汇总
     * @return 奖状汇总集合
     */
    public List<AwardRecords> selectAwardRecordsList(AwardRecords awardRecords);

    /**
     * 新增奖状汇总
     * 
     * @param awardRecords 奖状汇总
     * @return 结果
     */
    public int insertAwardRecords(AwardRecords awardRecords);

    /**
     * 修改奖状汇总
     * 
     * @param awardRecords 奖状汇总
     * @return 结果
     */
    public int updateAwardRecords(AwardRecords awardRecords);

    /**
     * 删除奖状汇总
     * 
     * @param id 奖状汇总主键
     * @return 结果
     */
    public int deleteAwardRecordsById(Long id);

    /**
     * 批量删除奖状汇总
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAwardRecordsByIds(Long[] ids);

    List<AwardRecords> getAwardByUserId(Long id);
}
