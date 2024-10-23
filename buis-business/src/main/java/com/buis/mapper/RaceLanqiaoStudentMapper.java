package com.buis.mapper;

import java.util.List;
import com.buis.domain.RaceLanqiaoStudent;

/**
 * 蓝桥杯学生报名信息Mapper接口
 * 
 * @author buis
 * @date 2024-10-23
 */
public interface RaceLanqiaoStudentMapper 
{
    /**
     * 查询蓝桥杯学生报名信息
     * 
     * @param id 蓝桥杯学生报名信息主键
     * @return 蓝桥杯学生报名信息
     */
    public RaceLanqiaoStudent selectRaceLanqiaoStudentById(Long id);

    /**
     * 查询蓝桥杯学生报名信息列表
     * 
     * @param raceLanqiaoStudent 蓝桥杯学生报名信息
     * @return 蓝桥杯学生报名信息集合
     */
    public List<RaceLanqiaoStudent> selectRaceLanqiaoStudentList(RaceLanqiaoStudent raceLanqiaoStudent);

    /**
     * 新增蓝桥杯学生报名信息
     * 
     * @param raceLanqiaoStudent 蓝桥杯学生报名信息
     * @return 结果
     */
    public int insertRaceLanqiaoStudent(RaceLanqiaoStudent raceLanqiaoStudent);

    /**
     * 修改蓝桥杯学生报名信息
     * 
     * @param raceLanqiaoStudent 蓝桥杯学生报名信息
     * @return 结果
     */
    public int updateRaceLanqiaoStudent(RaceLanqiaoStudent raceLanqiaoStudent);

    /**
     * 删除蓝桥杯学生报名信息
     * 
     * @param id 蓝桥杯学生报名信息主键
     * @return 结果
     */
    public int deleteRaceLanqiaoStudentById(Long id);

    /**
     * 批量删除蓝桥杯学生报名信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRaceLanqiaoStudentByIds(Long[] ids);
}
