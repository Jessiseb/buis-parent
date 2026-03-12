package com.buis.mapper;

import java.util.List;
import com.buis.model.domain.RaceLanqiaoTeacher;
import com.buis.model.vo.RaceLanqiaoTeacherVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/**
 * 蓝桥杯老师报名信息Mapper接口
 * 
 * @author buis
 * @date 2024-10-24
 */
public interface RaceLanqiaoTeacherMapper 
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
    public List<RaceLanqiaoTeacherVo> selectRaceLanqiaoTeacherList(RaceLanqiaoTeacher raceLanqiaoTeacher);

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
     * 删除蓝桥杯老师报名信息
     * 
     * @param id 蓝桥杯老师报名信息主键
     * @return 结果
     */
    public int deleteRaceLanqiaoTeacherById(Long id);

    /**
     * 批量删除蓝桥杯老师报名信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRaceLanqiaoTeacherByIds(Long[] ids);

    /**
     * 根据用户id查询报名信息
     * @param loginId
     * @return
     */
    RaceLanqiaoTeacher selectRaceLanqiaoTeacherByUserId(@Param("userId") Long loginId);

    /**
     * 根据用户id删除报名信息
     * @param loginId
     * @return
     */
    int deleteRaceLanqiaoTeacherByUserId(@Param("userId")Long loginId);
}
