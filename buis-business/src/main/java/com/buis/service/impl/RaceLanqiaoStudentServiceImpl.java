package com.buis.service.impl;

import java.util.List;

import com.buis.common.exception.ServiceException;
import com.buis.common.utils.DateUtils;
import com.buis.domain.RaceLanqiaoTeacher;
import com.buis.service.IRaceLanqiaoTeacherService;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.buis.mapper.RaceLanqiaoStudentMapper;
import com.buis.domain.RaceLanqiaoStudent;
import com.buis.service.IRaceLanqiaoStudentService;

/**
 * 蓝桥杯学生报名信息Service业务层处理
 * 
 * @author buis
 * @date 2024-10-24
 */
@Service
public class RaceLanqiaoStudentServiceImpl implements IRaceLanqiaoStudentService 
{
    @Autowired
    private RaceLanqiaoStudentMapper raceLanqiaoStudentMapper;
    @Autowired
    private IRaceLanqiaoTeacherService lanqiaoTeacherService;

    /**
     * 查询蓝桥杯学生报名信息
     * 
     * @param id 蓝桥杯学生报名信息主键
     * @return 蓝桥杯学生报名信息
     */
    @Override
    public RaceLanqiaoStudent selectRaceLanqiaoStudentById(Long id)
    {
        return raceLanqiaoStudentMapper.selectRaceLanqiaoStudentById(id);
    }

    /**
     * 查询蓝桥杯学生报名信息列表
     * 
     * @param raceLanqiaoStudent 蓝桥杯学生报名信息
     * @return 蓝桥杯学生报名信息
     */
    @Override
    public List<RaceLanqiaoStudent> selectRaceLanqiaoStudentList(RaceLanqiaoStudent raceLanqiaoStudent)
    {
        return raceLanqiaoStudentMapper.selectRaceLanqiaoStudentList(raceLanqiaoStudent);
    }

    /**
     * 新增蓝桥杯学生报名信息
     * 
     * @param raceLanqiaoStudent 蓝桥杯学生报名信息
     * @return 结果
     */
    @Override
    public int insertRaceLanqiaoStudent(RaceLanqiaoStudent raceLanqiaoStudent)
    {
        RaceLanqiaoTeacher raceLanqiaoTeacher = lanqiaoTeacherService.selectRaceLanqiaoTeacherByUserId(raceLanqiaoStudent.getUserId());
        if (raceLanqiaoTeacher!=null) {
            throw new ServiceException("已提交过老师报名表，请取消后再次提交");
        }
        raceLanqiaoStudent.setCreateTime(DateUtils.getNowDate());
        return raceLanqiaoStudentMapper.insertRaceLanqiaoStudent(raceLanqiaoStudent);
    }

    /**
     * 修改蓝桥杯学生报名信息
     * 
     * @param raceLanqiaoStudent 蓝桥杯学生报名信息
     * @return 结果
     */
    @Override
    public int updateRaceLanqiaoStudent(RaceLanqiaoStudent raceLanqiaoStudent)
    {
        raceLanqiaoStudent.setUpdateTime(DateUtils.getNowDate());
        return raceLanqiaoStudentMapper.updateRaceLanqiaoStudent(raceLanqiaoStudent);
    }

    /**
     * 批量删除蓝桥杯学生报名信息
     * 
     * @param ids 需要删除的蓝桥杯学生报名信息主键
     * @return 结果
     */
    @Override
    public int deleteRaceLanqiaoStudentByIds(Long[] ids)
    {
        return raceLanqiaoStudentMapper.deleteRaceLanqiaoStudentByIds(ids);
    }

    /**
     * 删除蓝桥杯学生报名信息信息
     * 
     * @param id 蓝桥杯学生报名信息主键
     * @return 结果
     */
    @Override
    public int deleteRaceLanqiaoStudentById(Long id)
    {
        return raceLanqiaoStudentMapper.deleteRaceLanqiaoStudentById(id);
    }


    //    获取蓝桥杯学生报名信息详细信息
    @Override
    public RaceLanqiaoStudent selectRaceLanqiaoStudentByUserId(Long loginId) {
        RaceLanqiaoStudent raceLanqiaoStudent = raceLanqiaoStudentMapper.selectRaceLanqiaoStudentByUserId(loginId);
        return raceLanqiaoStudent;
    }

    @Override
    public int deleteRaceLanqiaoStudentByUserId(Long loginId) {
       return raceLanqiaoStudentMapper.deleteRaceLanqiaoStudentByUserId(loginId);
    }
}
