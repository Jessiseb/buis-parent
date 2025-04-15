package com.buis.mapper;

import java.util.List;


import com.buis.domain.Assistant;
import com.buis.vo.AssistantVo;
import com.buis.vo.UserInfoVo;

/**
 * 学生助理Mapper接口
 * 
 * @author buis
 * @date 2024-10-11
 */
public interface AssistantMapper{
    /**
     * 查询学生助理
     * 
     * @param id 学生助理主键
     * @return 学生助理
     */
    public Assistant selectAssistantById(Long id);

    /**
     * 查询学生助理列表
     * 
     * @param assistant 学生助理
     * @return 学生助理集合
     */
    public List<AssistantVo> selectAssistantList(Assistant assistant);

    /**
     * 新增学生助理
     * 
     * @param assistant 学生助理
     * @return 结果
     */
    public int insertAssistant(Assistant assistant);

    /**
     * 修改学生助理
     * 
     * @param assistant 学生助理
     * @return 结果
     */
    public int updateAssistant(Assistant assistant);

    /**
     * 删除学生助理
     * 
     * @param id 学生助理主键
     * @return 结果
     */
    public int deleteAssistantById(Long id);

    /**
     * 批量删除学生助理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAssistantByIds(Long[] ids);

    /**
     * 联表模糊查询学生助理列表
     *
     * @param name
     */
    public List<AssistantVo> fuzzyQuery(String name);

    //根据活动id查询学生助理信息
    List<UserInfoVo> selectAssistantListByRace(Long id);
}
