package com.buis.service;

import java.util.List;
import com.buis.domain.Award;

/**
 * 奖状信息Service接口
 * 
 * @author zjx
 * @date 2025-03-16
 */
public interface IAwardService 
{
    /**
     * 查询奖状信息
     * 
     * @param id 奖状信息主键
     * @return 奖状信息
     */
    public  List<Award> selectAwardById(Long id);

    /**
     * 查询奖状信息列表
     * 
     * @param award 奖状信息
     * @return 奖状信息集合
     */
    public List<Award> selectAwardList(Award award);

    /**
     * 新增奖状信息
     * 
     * @param award 奖状信息
     * @return 结果
     */
    public int insertAward(Award award);

    /**
     * 修改奖状信息
     * 
     * @param award 奖状信息
     * @return 结果
     */
    public int updateAward(Award award);

    /**
     * 批量删除奖状信息
     * 
     * @param ids 需要删除的奖状信息主键集合
     * @return 结果
     */
    public int deleteAwardByIds(Long[] ids);

    /**
     * 删除奖状信息信息
     * 
     * @param id 奖状信息主键
     * @return 结果
     */
    public int deleteAwardById(Long id);

    Award selectAwardByName(String name,String level);
}
