package com.buis.service.impl;

import java.util.List;

import com.buis.common.core.domain.R;
import com.buis.common.exception.ServiceException;
import com.buis.common.utils.DateUtils;
import com.buis.common.utils.StringUtils;
import com.buis.domain.*;
import com.buis.service.*;
import com.buis.vo.RaceLanqiaoTeacherVo;
import com.buis.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.buis.mapper.RaceLanqiaoTeacherMapper;
import org.springframework.transaction.annotation.Transactional;

/**
 * 蓝桥杯老师报名信息Service业务层处理
 *
 * @author buis
 * @date 2024-10-24
 */
@Service
public class RaceLanqiaoTeacherServiceImpl implements IRaceLanqiaoTeacherService {
    @Autowired
    private RaceLanqiaoTeacherMapper raceLanqiaoTeacherMapper;
    @Autowired
    private IRaceLanqiaoStudentService lanqiaoStudentService;
    @Autowired
    private ITeamInfoService teamInfoService;
    @Autowired
    private IPreSignTeacherService preSignTeacherService;
    @Autowired
    private IUserInfoService userInfoService;

    /**
     * 查询蓝桥杯老师报名信息
     *
     * @param id 蓝桥杯老师报名信息主键
     * @return 蓝桥杯老师报名信息
     */
    @Override
    public RaceLanqiaoTeacher selectRaceLanqiaoTeacherById(Long id) {
        return raceLanqiaoTeacherMapper.selectRaceLanqiaoTeacherById(id);
    }

    /**
     * 查询蓝桥杯老师报名信息列表
     *
     * @param raceLanqiaoTeacher 蓝桥杯老师报名信息
     * @return 蓝桥杯老师报名信息
     */
    @Override
    public List<RaceLanqiaoTeacherVo> selectRaceLanqiaoTeacherList(RaceLanqiaoTeacher raceLanqiaoTeacher) {
        return raceLanqiaoTeacherMapper.selectRaceLanqiaoTeacherList(raceLanqiaoTeacher);
    }

    /**
     * 新增蓝桥杯老师报名信息
     *
     * @param raceLanqiaoTeacher 蓝桥杯老师报名信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertRaceLanqiaoTeacher(RaceLanqiaoTeacher raceLanqiaoTeacher) {
        UserInfo id = userInfoService.selectUserInfoById(raceLanqiaoTeacher.getUserId());
        if (id != null && id.getIsTeacher() != 1 ) {
            throw new ServiceException("您不是老师，无法报名");
        }
        //获取银行卡号
        String bankNumber = raceLanqiaoTeacher.getBankNumber();
        //银行卡号不能超过20
        if(StringUtils.isNotEmpty(bankNumber) && bankNumber.length()>20){
            throw new ServiceException("银行卡号过长!");
        }
        raceLanqiaoTeacher.setCreateTime(DateUtils.getNowDate());
        raceLanqiaoTeacherMapper.insertRaceLanqiaoTeacher(raceLanqiaoTeacher);

        //新增队伍
//        TeamInfo teamInfo = new TeamInfo();
//        teamInfo.setRace(raceLanqiaoTeacher.getRaceId());
//        teamInfo.setLeader(raceLanqiaoTeacher.getUserId());
//        teamInfoService.insertTeamInfo(teamInfo);

        //新增老师预报名
        PreSignTeacher preSignTeacher = new PreSignTeacher();
        preSignTeacher.setRace(raceLanqiaoTeacher.getRaceId());
        preSignTeacher.setTeacher(raceLanqiaoTeacher.getUserId());
        preSignTeacherService.insertPreSignTeacher(preSignTeacher);

        return 1;
    }

    /**
     * 修改蓝桥杯老师报名信息
     *
     * @param raceLanqiaoTeacher 蓝桥杯老师报名信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateRaceLanqiaoTeacher(RaceLanqiaoTeacher raceLanqiaoTeacher) {
        raceLanqiaoTeacher.setUpdateTime(DateUtils.getNowDate());
        //获取银行卡号
        String bankNumber = raceLanqiaoTeacher.getBankNumber();
        //银行卡号不能超过20
        if(StringUtils.isNotEmpty(bankNumber) && bankNumber.length()>20){
            throw new ServiceException("银行卡号过长!");
        }
        raceLanqiaoTeacherMapper.updateRaceLanqiaoTeacher(raceLanqiaoTeacher);
        return 1;
    }

    /**
     * 批量删除蓝桥杯老师报名信息
     *
     * @param ids 需要删除的蓝桥杯老师报名信息主键
     * @return 结果
     */
    @Override
    public int deleteRaceLanqiaoTeacherByIds(Long[] ids) {

        raceLanqiaoTeacherMapper.deleteRaceLanqiaoTeacherByIds(ids);


        return 1;
    }

    /**
     * 删除蓝桥杯老师报名信息信息
     *
     * @param id 蓝桥杯老师报名信息主键
     * @return 结果
     */
    @Override
    public int deleteRaceLanqiaoTeacherById(Long id) {
        raceLanqiaoTeacherMapper.deleteRaceLanqiaoTeacherById(id);
        return 1;
    }

    //查询比赛报名信息
    @Override
    public RaceLanqiaoTeacher selectRaceLanqiaoTeacherRaceIdAndByUserId(Long race, Long userId) {
        RaceLanqiaoTeacher raceLanqiaoTeacher = new RaceLanqiaoTeacher();
        raceLanqiaoTeacher.setRaceId(race);
        raceLanqiaoTeacher.setUserId(userId);
        List<RaceLanqiaoTeacherVo> raceLanqiaoTeachers = raceLanqiaoTeacherMapper.selectRaceLanqiaoTeacherList(raceLanqiaoTeacher);
        RaceLanqiaoTeacher lanqiaoTeacher = null;
        if (raceLanqiaoTeachers != null && raceLanqiaoTeachers.size() > 0) {
            lanqiaoTeacher = raceLanqiaoTeachers.get(0);
        }
        return lanqiaoTeacher;
    }

    //删除比赛报名信息
    @Override
    public Integer deleteRaceLanqiaoTeacherByRaceIdAndUserId(Long race, Long userId) {

        RaceLanqiaoTeacher lanqiaoTeacher = (RaceLanqiaoTeacher) selectRaceLanqiaoTeacherRaceIdAndByUserId(race, userId);
        if (lanqiaoTeacher != null) {
            int i = deleteRaceLanqiaoTeacherById(lanqiaoTeacher.getId());
            //删除队伍
            teamInfoService.deleteTeamInfoByRaceIdAndUserId(race, userId);
            //删除预报名
            preSignTeacherService.deletePreSignTeacherByRaceIdAndUserId(race, userId);
        }
        return 1;
    }
}
