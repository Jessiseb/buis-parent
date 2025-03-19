package com.buis.service;

import java.util.List;
import com.buis.domain.PreSignStudent;
import com.buis.vo.JudgeVo;
import com.buis.vo.PreSignStudentVo;

/**
 * 学生预报名Service接口
 * 
 * @author buis
 * @date 2024-10-11
 */
public interface IPreSignStudentService 
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
     * 批量删除学生预报名
     * 
     * @param ids 需要删除的学生预报名主键集合
     * @return 结果
     */
    public int deletePreSignStudentByIds(Long[] ids);

    /**
     * 删除学生预报名信息
     * 
     * @param id 学生预报名主键
     * @return 结果
     */
    public int deletePreSignStudentById(Long id);

    void deletePreSignStudentByRaceIdAndUserId(Long race, Long userId);

    List<PreSignStudentVo> fuzzyQuery(String name);
}
