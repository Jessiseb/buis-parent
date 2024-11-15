package com.buis.mapper;

import java.util.List;


import com.buis.domain.Judge;
import com.buis.domain.PreSignStudent;
import com.buis.vo.PreSignStudentVo;
import org.apache.ibatis.annotations.Param;

/**
 * 学生预报名Mapper接口
 * 
 * @author buis
 * @date 2024-10-11
 */
public interface PreSignStudentMapper
{
    /**
     * 查询学生预报名
     * 
     * @param id 学生预报名主键
     * @return 学生预报名
     */
    public PreSignStudentVo selectPreSignStudentById(Long id);

    /**
     * 查询学生预报名列表
     * 
     * @param preSignStudent 学生预报名
     * @return 学生预报名集合
     */
    public List<PreSignStudentVo> selectPreSignStudentList(PreSignStudent preSignStudent);

    /**
     * 新增学生预报名
     * 
     * @param preSignStudent 学生预报名
     * @return 结果
     */
    public int insertPreSignStudent(PreSignStudent preSignStudent);

    /**
     * 修改学生预报名
     * 
     * @param preSignStudent 学生预报名
     * @return 结果
     */
    public int updatePreSignStudent(PreSignStudent preSignStudent);

    /**
     * 删除学生预报名
     * 
     * @param id 学生预报名主键
     * @return 结果
     */
    public int deletePreSignStudentById(Long id);

    /**
     * 批量删除学生预报名
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePreSignStudentByIds(Long[] ids);

    /**
     * 删除学生预报名
     * @param race
     * @param userId
     */
    void deletePreSignStudentByUserIdAndRaceId(@Param("race") Long race, @Param("userId") Long userId);
}
