package com.buis.service.impl;

import java.util.List;
import com.buis.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.buis.mapper.PreSignTeacherMapper;
import com.buis.domain.PreSignTeacher;
import com.buis.service.IPreSignTeacherService;

/**
 * 老师预报名Service业务层处理
 * 
 * @author buis
 * @date 2024-10-11
 */
@Service
public class PreSignTeacherServiceImpl implements IPreSignTeacherService 
{
    @Autowired
    private PreSignTeacherMapper preSignTeacherMapper;

    /**
     * 查询老师预报名
     * 
     * @param id 老师预报名主键
     * @return 老师预报名
     */
    @Override
    public PreSignTeacher selectPreSignTeacherById(Long id)
    {
        return preSignTeacherMapper.selectPreSignTeacherById(id);
    }

    /**
     * 查询老师预报名列表
     * 
     * @param preSignTeacher 老师预报名
     * @return 老师预报名
     */
    @Override
    public List<PreSignTeacher> selectPreSignTeacherList(PreSignTeacher preSignTeacher)
    {
        return preSignTeacherMapper.selectPreSignTeacherList(preSignTeacher);
    }

    /**
     * 新增老师预报名
     * 
     * @param preSignTeacher 老师预报名
     * @return 结果
     */
    @Override
    public int insertPreSignTeacher(PreSignTeacher preSignTeacher)
    {
        preSignTeacher.setCreateTime(DateUtils.getNowDate());
        return preSignTeacherMapper.insertPreSignTeacher(preSignTeacher);
    }

    /**
     * 修改老师预报名
     * 
     * @param preSignTeacher 老师预报名
     * @return 结果
     */
    @Override
    public int updatePreSignTeacher(PreSignTeacher preSignTeacher)
    {
        preSignTeacher.setUpdateTime(DateUtils.getNowDate());
        return preSignTeacherMapper.updatePreSignTeacher(preSignTeacher);
    }

    /**
     * 批量删除老师预报名
     * 
     * @param ids 需要删除的老师预报名主键
     * @return 结果
     */
    @Override
    public int deletePreSignTeacherByIds(Long[] ids)
    {
        return preSignTeacherMapper.deletePreSignTeacherByIds(ids);
    }

    /**
     * 删除老师预报名信息
     * 
     * @param id 老师预报名主键
     * @return 结果
     */
    @Override
    public int deletePreSignTeacherById(Long id)
    {
        return preSignTeacherMapper.deletePreSignTeacherById(id);
    }
}
