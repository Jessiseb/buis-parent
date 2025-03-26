package com.buis.service;

import java.util.List;

import com.buis.common.core.domain.AjaxResult;
import com.buis.common.core.page.MyPageVo;
import com.buis.domain.RaceInfo;
import com.buis.dto.TeacherExportToDaDto;
import com.buis.dto.TeacherExportToDiDto;
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
     * 查询比赛信息
     * 
     * @param id 比赛信息主键
     * @return 比赛信息
     */
    public RaceInfoVo selectRaceInfoById(Long id);

    /**
     * 查询比赛信息列表
     * 
     * @param raceInfo 比赛信息
     * @return 比赛信息集合
     */
    public List<RaceInfoVo> selectRaceInfoList(RaceInfo raceInfo);

    /**
     * 新增比赛信息
     * 
     * @param raceInfo 比赛信息
     * @return 结果
     */
    public int insertRaceInfo(RaceInfo raceInfo);

    /**
     * 修改比赛信息
     * 
     * @param raceInfo 比赛信息
     * @return 结果
     */
    public int updateRaceInfo(RaceInfo raceInfo);

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

    AjaxResult updateApproval(Long id);

    /**
     * 检查是否超过了比赛的指导老师数量限制
     * @param raceId       比赛的ID
     * @param teacherId    老师的ID
     * @return
     */
    public boolean checkGuideLimit(Long raceId,Long teacherId);

    public List<RaceInfo> selectRaceInfoByStatu(Long i);

    List<RaceInfo> getUserRaceList(Long userId);

    List<TeacherExportToDaDto> exportTeacherDaProjectCount(List<TeacherExportToDaDto> teacherExporToDektDtoList,String name, HttpServletResponse response);

    List<TeacherExportToDiDto> exportTeacherDiProjectCount(List<TeacherExportToDiDto> teacherExporToDektDtoList, String name, HttpServletResponse response);

    /**
     * 判断对象的所有字段是否为空
     * @param obj
     * @return
     **/
    boolean allFieldsEmpty(Object obj);

    MyPageVo<List<RaceInfoVo>> selectRaceInfoListFromRedis(RaceInfo raceInfo);

    List<SecondClassCountVo> getSecondClassCountByMonth(Integer month);

    List<SecondClassCountVo> getSecondClassCountByYear(Integer year);
}
