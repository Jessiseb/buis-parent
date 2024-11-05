package com.buis.service.impl;

import java.util.List;

import com.buis.common.exception.ServiceException;
import com.buis.common.utils.DateUtils;
import com.buis.domain.RaceLanqiaoStudent;
import com.buis.service.IRaceLanqiaoStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.buis.mapper.RaceLanqiaoTeacherMapper;
import com.buis.domain.RaceLanqiaoTeacher;
import com.buis.service.IRaceLanqiaoTeacherService;

/**
 * 蓝桥杯老师报名信息Service业务层处理
 * 
 * @author buis
 * @date 2024-10-24
 */
@Service
public class RaceLanqiaoTeacherServiceImpl implements IRaceLanqiaoTeacherService 
{
    @Autowired
    private RaceLanqiaoTeacherMapper raceLanqiaoTeacherMapper;
    @Autowired
    private IRaceLanqiaoStudentService lanqiaoStudentService;
    /**
     * 查询蓝桥杯老师报名信息
     * 
     * @param id 蓝桥杯老师报名信息主键
     * @return 蓝桥杯老师报名信息
     */
    @Override
    public RaceLanqiaoTeacher selectRaceLanqiaoTeacherById(Long id)
    {
        return raceLanqiaoTeacherMapper.selectRaceLanqiaoTeacherById(id);
    }

    /**
     * 查询蓝桥杯老师报名信息列表
     * 
     * @param raceLanqiaoTeacher 蓝桥杯老师报名信息
     * @return 蓝桥杯老师报名信息
     */
    @Override
    public List<RaceLanqiaoTeacher> selectRaceLanqiaoTeacherList(RaceLanqiaoTeacher raceLanqiaoTeacher)
    {
        return raceLanqiaoTeacherMapper.selectRaceLanqiaoTeacherList(raceLanqiaoTeacher);
    }

    /**
     * 新增蓝桥杯老师报名信息
     * 
     * @param raceLanqiaoTeacher 蓝桥杯老师报名信息
     * @return 结果
     */
    @Override
    public int insertRaceLanqiaoTeacher(RaceLanqiaoTeacher raceLanqiaoTeacher)
    {
        RaceLanqiaoStudent raceLanqiaoStudent = lanqiaoStudentService.selectRaceLanqiaoStudentByUserId(raceLanqiaoTeacher.getUserId());
        if (raceLanqiaoStudent!=null) {
            throw new ServiceException("已提交过学生报名表，请取消后再次提交");
        }
        raceLanqiaoTeacher.setCreateTime(DateUtils.getNowDate());
        return raceLanqiaoTeacherMapper.insertRaceLanqiaoTeacher(raceLanqiaoTeacher);
    }

    /**
     * 修改蓝桥杯老师报名信息
     * 
     * @param raceLanqiaoTeacher 蓝桥杯老师报名信息
     * @return 结果
     */
    @Override
    public int updateRaceLanqiaoTeacher(RaceLanqiaoTeacher raceLanqiaoTeacher)
    {
        raceLanqiaoTeacher.setUpdateTime(DateUtils.getNowDate());
        return raceLanqiaoTeacherMapper.updateRaceLanqiaoTeacher(raceLanqiaoTeacher);
    }

    /**
     * 批量删除蓝桥杯老师报名信息
     * 
     * @param ids 需要删除的蓝桥杯老师报名信息主键
     * @return 结果
     */
    @Override
    public int deleteRaceLanqiaoTeacherByIds(Long[] ids)
    {
        return raceLanqiaoTeacherMapper.deleteRaceLanqiaoTeacherByIds(ids);
    }

    /**
     * 删除蓝桥杯老师报名信息信息
     * 
     * @param id 蓝桥杯老师报名信息主键
     * @return 结果
     */
    @Override
    public int deleteRaceLanqiaoTeacherById(Long id)
    {
        return raceLanqiaoTeacherMapper.deleteRaceLanqiaoTeacherById(id);
    }

    @Override
    public RaceLanqiaoTeacher selectRaceLanqiaoTeacherByUserId(Long loginId) {
        RaceLanqiaoTeacher raceLanqiaoTeacher = raceLanqiaoTeacherMapper.selectRaceLanqiaoTeacherByUserId(loginId);
        return raceLanqiaoTeacher;
    }

    @Override
    public int deleteRaceLanqiaoTeacherByUserId(Long loginId) {
        return raceLanqiaoTeacherMapper.deleteRaceLanqiaoTeacherByUserId(loginId);
    }
}
