package com.buis.mapper;

import java.util.List;


import com.buis.domain.PreSignTeacher;
import com.buis.vo.PreSignTeacherVo;

/**
 * 老师预报名Mapper接口
 * 
 * @author buis
 * @date 2024-10-11
 */
public interface PreSignTeacherMapper
{
    /*
     * 查询老师预报名
     * 
     * @param id 老师预报名主键
     * @return 老师预报名
     */
    public PreSignTeacherVo selectPreSignTeacherById(Long id);

    /**
     * 查询老师预报名列表
     * 
     * @param preSignTeacher 老师预报名
     * @return 老师预报名集合
     */
    public List<PreSignTeacherVo> selectPreSignTeacherList(PreSignTeacher preSignTeacher);

    /**
     * 新增老师预报名
     * 
     * @param preSignTeacher 老师预报名
     * @return 结果
     */
    public int insertPreSignTeacher(PreSignTeacher preSignTeacher);

    /**
     * 修改老师预报名
     * 
     * @param preSignTeacher 老师预报名
     * @return 结果
     */
    public int updatePreSignTeacher(PreSignTeacher preSignTeacher);

    /**
     * 删除老师预报名
     * 
     * @param id 老师预报名主键
     * @return 结果
     */
    public int deletePreSignTeacherById(Long id);

    /**
     * 批量删除老师预报名
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePreSignTeacherByIds(Long[] ids);

}
