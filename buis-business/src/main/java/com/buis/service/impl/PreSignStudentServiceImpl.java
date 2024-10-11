package com.buis.service.impl;

import java.util.List;
import com.buis.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.buis.mapper.PreSignStudentMapper;
import com.buis.domain.PreSignStudent;
import com.buis.service.IPreSignStudentService;

/**
 * 学生预报名Service业务层处理
 * 
 * @author buis
 * @date 2024-10-11
 */
@Service
public class PreSignStudentServiceImpl implements IPreSignStudentService 
{
    @Autowired
    private PreSignStudentMapper preSignStudentMapper;

    /**
     * 查询学生预报名
     * 
     * @param id 学生预报名主键
     * @return 学生预报名
     */
    @Override
    public PreSignStudent selectPreSignStudentById(Long id)
    {
        return preSignStudentMapper.selectPreSignStudentById(id);
    }

    /**
     * 查询学生预报名列表
     * 
     * @param preSignStudent 学生预报名
     * @return 学生预报名
     */
    @Override
    public List<PreSignStudent> selectPreSignStudentList(PreSignStudent preSignStudent)
    {
        return preSignStudentMapper.selectPreSignStudentList(preSignStudent);
    }

    /**
     * 新增学生预报名
     * 
     * @param preSignStudent 学生预报名
     * @return 结果
     */
    @Override
    public int insertPreSignStudent(PreSignStudent preSignStudent)
    {
        preSignStudent.setCreateTime(DateUtils.getNowDate());
        return preSignStudentMapper.insertPreSignStudent(preSignStudent);
    }

    /**
     * 修改学生预报名
     * 
     * @param preSignStudent 学生预报名
     * @return 结果
     */
    @Override
    public int updatePreSignStudent(PreSignStudent preSignStudent)
    {
        preSignStudent.setUpdateTime(DateUtils.getNowDate());
        return preSignStudentMapper.updatePreSignStudent(preSignStudent);
    }

    /**
     * 批量删除学生预报名
     * 
     * @param ids 需要删除的学生预报名主键
     * @return 结果
     */
    @Override
    public int deletePreSignStudentByIds(Long[] ids)
    {
        return preSignStudentMapper.deletePreSignStudentByIds(ids);
    }

    /**
     * 删除学生预报名信息
     * 
     * @param id 学生预报名主键
     * @return 结果
     */
    @Override
    public int deletePreSignStudentById(Long id)
    {
        return preSignStudentMapper.deletePreSignStudentById(id);
    }
}
