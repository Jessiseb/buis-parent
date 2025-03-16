package com.buis.mapper;

import java.util.List;


import com.buis.domain.CollegeMajor;
import com.buis.domain.Judge;
import com.buis.vo.JudgeVo;

/**
 * 评委管理Mapper接口
 * 
 * @author buis
 * @date 2024-10-10
 */
public interface JudgeMapper
{
    /**
     * 查询评委管理
     * 
     * @param id 评委管理主键
     * @return 评委管理
     */
    public Judge selectJudgeById(Long id);

    /**
     * 查询评委管理列表
     * 
     * @param judge 评委管理
     * @return 评委管理集合
     */
    public List<JudgeVo> selectJudgeList(Judge judge);

    /**
     * 新增评委管理
     * 
     * @param judge 评委管理
     * @return 结果
     */
    public int insertJudge(Judge judge);

    /**
     * 修改评委管理
     * 
     * @param judge 评委管理
     * @return 结果
     */
    public int updateJudge(Judge judge);

    /**
     * 删除评委管理
     * 
     * @param id 评委管理主键
     * @return 结果
     */
    public int deleteJudgeById(Long id);

    /**
     * 批量删除评委管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteJudgeByIds(Long[] ids);

    /**
     * 联表模糊查询评委列表
     *
     * @param name
     */
    public List<JudgeVo> fuzzyQuery(String name);

    /**
     * 根据教师ID查询评委信息
     * @param teacherId
     * @return
     */
    public Judge selectJudgeByTeacherId(Long teacherId);
}
