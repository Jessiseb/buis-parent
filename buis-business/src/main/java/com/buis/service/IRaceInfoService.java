package com.buis.service;

import java.util.List;

import com.buis.common.core.domain.AjaxResult;
import com.buis.common.core.page.MyPageVo;
import com.buis.domain.RaceInfo;
import com.buis.dto.GradeStatistic;
import com.buis.dto.StudentExportToDiDto;
import com.buis.dto.TeacherExportToDaDto;
import com.buis.dto.TeacherExportToDiDto;
import com.buis.vo.HotRaceVo;
import com.buis.vo.RaceInfoVo;
import com.buis.vo.SecondClassCountVo;

import javax.servlet.http.HttpServletResponse;

/**
 * 比赛信息Service接口
 * 
 * @author kaven
 * @date 2024-10-23
 */
public interface IRaceInfoService 
{
    /**
     * 前台查询比赛信息
     * 
     * @param id 比赛信息主键
     * @return 比赛信息
     */
    public RaceInfoVo selectRaceInfoById(Long id);

    /**
     * 通用查询比赛信息
     */
    public RaceInfoVo selectRaceInfoVoById(Long id);

    /**
     * 管理系统查询比赛信息列表
     * 
     * @param raceInfo 比赛信息
     * @return 比赛信息集合
     */
    public List<RaceInfoVo> selectRaceInfoList(RaceInfo raceInfo);

    /**
     * 前台获取比赛信息列表(只能查出已经发布了赛事规则的比赛信息)
     *
     */
    public List<RaceInfoVo> getRaceInfoList(RaceInfo raceInfo);

    /**
     * 新增比赛信息
     * 
     * @param raceInfo 比赛信息
     * @return 结果
     */
    public int insertRaceInfo(RaceInfo raceInfo);

    /**
     * 前台修改比赛信息
     * 
     * @param raceInfo 比赛信息
     * @return 结果
     */
    public int updateRaceInfo(RaceInfo raceInfo);

    /**
     * 后台修改比赛信息
     */
    public int updateBackRaceInfo(RaceInfo raceInfo);

    /**
     * 批量删除比赛信息
     * 
     * @param ids 需要删除的比赛信息主键集合
     * @return 结果
     */
    public int deleteRaceInfoByIds(Long[] ids);

    /**
     * 删除比赛信息信息
     * 
     * @param id 比赛信息主键
     * @return 结果
     */
    public int deleteRaceInfoById(Long id);

    /**
     * 判断比赛是否已结束
     * @param raceId
     * @return
     */
    public boolean checkJoinCondition(Long userId,Long raceId);

//    AjaxResult updateApproval(Long id);

    /**
     * 检查是否超过了比赛的指导老师数量限制
     * @param raceId       比赛的ID
     * @param teacherId    老师的ID
     * @return
     */
    public boolean checkGuideLimit(Long raceId,Long teacherId);

    public List<RaceInfo> selectRaceInfoByStatu(Long i);

    List<RaceInfo> getUserRaceList(Long userId);

    List<TeacherExportToDaDto> exportTeacherDaProjectCount(int college,int grade,List<TeacherExportToDaDto> teacherExporToDektDtoList,String name, HttpServletResponse response);

    List<TeacherExportToDiDto> exportTeacherDiProjectCount(int college,int grade,List<TeacherExportToDiDto> teacherExporToDektDtoList, String name, HttpServletResponse response);

    /**
     * 判断对象的所有字段是否为空
     * @param obj
     * @return
     **/
    boolean allFieldsEmpty(Object obj);

    MyPageVo<List<RaceInfoVo>> selectRaceInfoListFromRedis(RaceInfo raceInfo);

    List<SecondClassCountVo> getSecondClassCountByMonth(Integer month);

    List<SecondClassCountVo> getSecondClassCountByYear(Integer year);

    /**
     * 获取热门比赛
     * @return
     */
    List<HotRaceVo> getHotRace();
//    List<TeacherExportToDiDto> exportTeacherDiProjectCount(List<TeacherExportToDiDto> teacherExporToDektDtoList, String name, HttpServletResponse response);

    List<StudentExportToDiDto> exportStudentDiProjectCount(int college,int grade,List<StudentExportToDiDto> StudentExporToDektDtoList, String name, HttpServletResponse response);

    //查询个人年度参与活动率
    String userAnnualParticipationRate(Long userId, int grade);

    //分页查询第二课堂活动申报
    List<RaceInfo> selectSecondActivities(RaceInfo raceInfo);

    //统计各年级学生的参与第二课堂的活动占比
    List<GradeStatistic> getStudentGradeCount();

    //查询第二课堂教师指导项目数
    String getTeacherInstructCountFromSecond(Long userId);

    RaceInfo selectRaceInfoByName(String name);

    void deleteRacesInfoCache();

    void deleteJoinInfoCache(Long userId);
}
