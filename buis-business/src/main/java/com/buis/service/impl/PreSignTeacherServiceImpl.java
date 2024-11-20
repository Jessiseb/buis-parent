package com.buis.service.impl;

import java.util.List;
import com.buis.common.utils.DateUtils;
import com.buis.service.IUserInfoService;
import com.buis.vo.PreSignTeacherVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.buis.mapper.PreSignTeacherMapper;
import com.buis.domain.PreSignTeacher;
import com.buis.service.IPreSignTeacherService;

import javax.annotation.Resource;

/**
 * 老师预报名Service业务层处理
 * 
 * @author buis
 * @date 2024-10-11
 */
@Service
public class PreSignTeacherServiceImpl implements IPreSignTeacherService 
{
    @Resource
    private IUserInfoService userInfoService;
    @Autowired
    private PreSignTeacherMapper preSignTeacherMapper;

    /**
     * 查询老师预报名
     *
     * @param id 老师预报名主键
     * @return 老师预报名
     */
    @Override
    public PreSignTeacherVo selectPreSignTeacherById(Long id) {
        PreSignTeacherVo preSignTeacher = preSignTeacherMapper.selectPreSignTeacherById(id);
        setTeacherInfo(preSignTeacher);
        return preSignTeacher;
    }

    /**
     * 查询老师预报名列表
     *
     * @param preSignTeacher 老师预报名
     * @return 老师预报名列表
     */
    @Override
    public List<PreSignTeacherVo> selectPreSignTeacherList(PreSignTeacher preSignTeacher) {
        List<PreSignTeacherVo> preSignTeachers = preSignTeacherMapper.selectPreSignTeacherList(preSignTeacher);
        for (PreSignTeacherVo signTeacher : preSignTeachers) {
            setTeacherInfo(signTeacher);
        }
        return preSignTeachers;
    }

    /**
     * 设置老师信息
     *
     * @param preSignTeacherVo 老师预报名对象
     */
    private void setTeacherInfo(PreSignTeacherVo preSignTeacherVo) {
        if (preSignTeacherVo != null) {
            preSignTeacherVo.setTeacherInfo(userInfoService.selectUserInfoById(preSignTeacherVo.getTeacher()));
        }
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

    @Override
    public void deletePreSignTeacherByRaceIdAndUserId(Long race, Long userId) {
        preSignTeacherMapper.deletePreSignTeacherByUserIdAndRaceId(race, userId);
    }

}
