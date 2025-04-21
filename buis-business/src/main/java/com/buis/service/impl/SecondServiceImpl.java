package com.buis.service.impl;

import java.util.List;
import com.buis.common.utils.DateUtils;
import com.buis.vo.RaceSecondVo;
import org.springframework.stereotype.Service;
import com.buis.mapper.SecondMapper;
import com.buis.domain.Second;
import com.buis.service.ISecondService;

import javax.annotation.Resource;

/**
 * 关联第二课堂表Service业务层处理
 * 
 * @author buis
 * @date 2025-04-04
 */
@Service
public class SecondServiceImpl implements ISecondService
{
    @Resource
    private SecondMapper secondMapper;

    /**
     * 查询关联第二课堂表
     * 
     * @param id 关联第二课堂表主键
     * @return 关联第二课堂表
     */
    @Override
    public Second selectSecondById(Long id)
    {
        return secondMapper.selectSecondById(id);
    }

    /**
     * 查询关联第二课堂表列表
     * 
     * @param second 关联第二课堂表
     * @return 关联第二课堂表
     */
    @Override
    public List<Second> selectSecondList(Second second)
    {
        return secondMapper.selectSecondList(second);
    }

    /**
     * 新增关联第二课堂表
     * 
     * @param second 关联第二课堂表
     * @return 结果
     */
    @Override
    public int insertSecond(Second second)
    {
        second.setCreateTime(DateUtils.getNowDate());
        second.setIsDeleted(0L);
        return secondMapper.insertSecond(second);
    }

    /**
     * 修改关联第二课堂表
     * 
     * @param second 关联第二课堂表
     * @return 结果
     */
    @Override
    public int updateSecond(Second second)
    {
        second.setUpdateTime(DateUtils.getNowDate());
        return secondMapper.updateSecond(second);
    }

    /**
     * 批量删除关联第二课堂表
     * 
     * @param ids 需要删除的关联第二课堂表主键
     * @return 结果
     */
    @Override
    public int deleteSecondByIds(Long[] ids)
    {
        return secondMapper.deleteSecondByIds(ids);
    }

    /**
     * 删除关联第二课堂表信息
     * 
     * @param id 关联第二课堂表主键
     * @return 结果
     */
    @Override
    public int deleteSecondById(Long id)
    {
        return secondMapper.deleteSecondById(id);
    }
}
