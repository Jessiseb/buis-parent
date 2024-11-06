package com.buis.service;

import java.util.List;
import com.buis.domain.RaceLanqiaoTeacher;

/**
 * 蓝桥杯老师报名信息Service接口
 * 
 * @author buis
 * @date 2024-10-24
 */
public interface IRaceLanqiaoTeacherService 
{
    /**
     * 查询蓝桥杯老师报名信息
     * 
     * @param id 蓝桥杯老师报名信息主键
     * @return 蓝桥杯老师报名信息
     */
    public RaceLanqiaoTeacher selectRaceLanqiaoTeacherById(Long id);

    /**
     * 查询蓝桥杯老师报名信息列表
     * 
     * @param raceLanqiaoTeacher 蓝桥杯老师报名信息
     * @return 蓝桥杯老师报名信息集合
     */
    public List<RaceLanqiaoTeacher> selectRaceLanqiaoTeacherList(RaceLanqiaoTeacher raceLanqiaoTeacher);

    /**
     * 新增蓝桥杯老师报名信息
     * 
     * @param raceLanqiaoTeacher 蓝桥杯老师报名信息
     * @return 结果
     */
    public int insertRaceLanqiaoTeacher(RaceLanqiaoTeacher raceLanqiaoTeacher);

    /**
     * 修改蓝桥杯老师报名信息
     * 
     * @param raceLanqiaoTeacher 蓝桥杯老师报名信息
     * @return 结果
     */
    public int updateRaceLanqiaoTeacher(RaceLanqiaoTeacher raceLanqiaoTeacher);

    /**
     * 批量删除蓝桥杯老师报名信息
     * 
     * @param ids 需要删除的蓝桥杯老师报名信息主键集合
     * @return 结果
     */
    public int deleteRaceLanqiaoTeacherByIds(Long[] ids);

    /**
     * 删除蓝桥杯老师报名信息信息
     * 
     * @param id 蓝桥杯老师报名信息主键
     * @return 结果
     */
    public int deleteRaceLanqiaoTeacherById(Long id);

    public Object selectRaceLanqiaoTeacherByUserIdAndRaceId(Long race, Long userId);

    public Integer deleteRaceLanqiaoTeacherByUserIdAndRaceId(Long race, Long userId);
}
