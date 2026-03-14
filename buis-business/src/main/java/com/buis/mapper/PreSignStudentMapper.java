package com.buis.mapper;

import java.util.List;
import java.util.Map;


import com.buis.model.domain.PreSignStudent;
import com.buis.model.vo.PreSignStudentVo;
import com.buis.model.vo.TrendResultVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 学生预报名Mapper接口
 * 
 * @author buis
 * @date 2024-10-11
 */
public interface PreSignStudentMapper
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
     * 删除学生预报名
     * 
     * @param id 学生预报名主键
     * @return 结果
     */
    public int deletePreSignStudentById(Long id);

    /**
     * 批量删除学生预报名
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePreSignStudentByIds(Long[] ids);

    /**
     * 删除学生预报名
     * @param race
     * @param userId
     */
    void deletePreSignStudentByRaceIdAndUserId(@Param("race") Long race, @Param("userId") Long userId);

    List<PreSignStudentVo> fuzzyQuery(String name);

    PreSignStudent selectByUserIdAndRace(@Param("userId") Long userId,@Param("raceId")  Long raceId);

    List<PreSignStudentVo> selectPreSignStudentByRace(@Param("race") Long race);

    List<PreSignStudentVo> selectPreSignStudentByUserId(@Param("userId") Long userId);

    /**
     * 查询最近12个月的报名趋势数据
     */
    @Select("SELECT DATE_FORMAT(create_time, '%Y-%m') as month, COUNT(*) as count FROM pre_sign_student WHERE is_deleted = 0 GROUP BY DATE_FORMAT(create_time, '%Y-%m') ORDER BY month DESC LIMIT 12")
    List<TrendResultVo> selectMonthlyTrend();

}
