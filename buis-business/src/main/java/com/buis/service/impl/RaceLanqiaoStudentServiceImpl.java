package com.buis.service.impl;

import java.util.List;

import com.buis.common.exception.ServiceException;
import com.buis.common.utils.DateUtils;
import com.buis.common.utils.StringUtils;
import com.buis.domain.*;
import com.buis.service.IPreSignStudentService;
import com.buis.service.IRaceLanqiaoTeacherService;
import com.buis.service.ITeamInfoService;
import com.buis.vo.RaceLanqiaoStudentVo;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.buis.mapper.RaceLanqiaoStudentMapper;
import com.buis.service.IRaceLanqiaoStudentService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 蓝桥杯学生报名信息Service业务层处理
 *
 * @author buis
 * @date 2024-10-24
 */
@Service
public class RaceLanqiaoStudentServiceImpl implements IRaceLanqiaoStudentService {
    @Autowired
    private RaceLanqiaoStudentMapper raceLanqiaoStudentMapper;
    @Autowired
    private IRaceLanqiaoTeacherService lanqiaoTeacherService;
    @Autowired
    private ITeamInfoService teamInfoService;
    @Autowired
    private IPreSignStudentService preSignStudentService;

    /**
     * 查询蓝桥杯学生报名信息
     *
     * @param id 蓝桥杯学生报名信息主键
     * @return 蓝桥杯学生报名信息
     */
    @Override
    public RaceLanqiaoStudentVo selectRaceLanqiaoStudentById(Long id) {
        return raceLanqiaoStudentMapper.selectRaceLanqiaoStudentById(id);
    }

    /**
     * 查询蓝桥杯学生报名信息列表
     *
     * @param raceLanqiaoStudent 蓝桥杯学生报名信息
     * @return 蓝桥杯学生报名信息
     */
    @Override
    public List<RaceLanqiaoStudentVo> selectRaceLanqiaoStudentList(RaceLanqiaoStudent raceLanqiaoStudent) {
        return raceLanqiaoStudentMapper.selectRaceLanqiaoStudentList(raceLanqiaoStudent);
    }

    /**
     * 新增蓝桥杯学生报名信息
     *
     * @param raceLanqiaoStudent 蓝桥杯学生报名信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertRaceLanqiaoStudent(RaceLanqiaoStudent raceLanqiaoStudent) {
//        Object o = lanqiaoTeacherService.selectRaceLanqiaoTeacherByUserIdAndRaceId(raceLanqiaoStudent.getRaceId(), raceLanqiaoStudent.getUserId());
//        if (o != null) {
//            throw new ServiceException("已提交过老师报名表，请取消后再次提交");
//        }
//        Object o1 = selectRaceLanqiaoStudentByUserIdAndRaceId(raceLanqiaoStudent.getRaceId(), raceLanqiaoStudent.getUserId());
//        if (o1 != null) {
//            throw new ServiceException("已提交过学生报名表，请勿重复提交");
//        }
        //获取银行卡号
        String bankNumber = raceLanqiaoStudent.getBankNumber();
        //银行卡号不能超过20
        if(StringUtils.isNotEmpty(bankNumber) && bankNumber.length()>20){
            throw new ServiceException("银行卡号过长!");
        }
        raceLanqiaoStudent.setCreateTime(DateUtils.getNowDate());
        raceLanqiaoStudentMapper.insertRaceLanqiaoStudent(raceLanqiaoStudent);
        //新增队伍
        TeamInfo teamInfo = new TeamInfo();
        teamInfo.setRace(raceLanqiaoStudent.getRaceId());
        teamInfo.setLeader(raceLanqiaoStudent.getUserId());
        teamInfoService.insertTeamInfo(teamInfo);
        //新增学生预报名
        PreSignStudent preSignStudent = new PreSignStudent();
        preSignStudent.setRace(raceLanqiaoStudent.getRaceId());
        preSignStudent.setStudent(raceLanqiaoStudent.getUserId());
        preSignStudentService.insertPreSignStudent(preSignStudent);
        return 1;
    }


    /**
     * 修改蓝桥杯学生报名信息
     *
     * @param raceLanqiaoStudent 蓝桥杯学生报名信息
     * @return 结果
     */
    @Override
    public int updateRaceLanqiaoStudent(RaceLanqiaoStudent raceLanqiaoStudent) {
        raceLanqiaoStudent.setUpdateTime(DateUtils.getNowDate());
        //获取银行卡号
        String bankNumber = raceLanqiaoStudent.getBankNumber();
        //银行卡号不能超过20
        if(StringUtils.isNotEmpty(bankNumber) && bankNumber.length()>20){
            throw new ServiceException("银行卡号过长!");
        }
        return raceLanqiaoStudentMapper.updateRaceLanqiaoStudent(raceLanqiaoStudent);
    }

    /**
     * 批量删除蓝桥杯学生报名信息
     *
     * @param ids 需要删除的蓝桥杯学生报名信息主键
     * @return 结果
     */
    @Override
    public int deleteRaceLanqiaoStudentByIds(Long[] ids) {
        raceLanqiaoStudentMapper.deleteRaceLanqiaoStudentByIds(ids);

        return 1;
    }

    /**
     * 删除蓝桥杯学生报名信息信息
     *
     * @param id 蓝桥杯学生报名信息主键
     * @return 结果
     */
    @Override
    public int deleteRaceLanqiaoStudentById(Long id) {
        raceLanqiaoStudentMapper.deleteRaceLanqiaoStudentById(id);
        return 1;
    }

    @Override
    public RaceLanqiaoStudentVo selectRaceLanqiaoStudentByUserIdAndRaceId(Long race, Long userId) {
        RaceLanqiaoStudent raceLanqiaoStudent = new RaceLanqiaoStudent();
        raceLanqiaoStudent.setRaceId(race);
        raceLanqiaoStudent.setUserId(userId);
        List<RaceLanqiaoStudentVo> raceLanqiaoStudents = raceLanqiaoStudentMapper.selectRaceLanqiaoStudentList(raceLanqiaoStudent);
        RaceLanqiaoStudentVo LanqiaoStudent = null;
        if (raceLanqiaoStudents != null && raceLanqiaoStudents.size() > 0) {
            LanqiaoStudent = raceLanqiaoStudents.get(0);
        }
        return LanqiaoStudent;
    }

    @Override
    @Transactional
    public Integer deleteRaceLanqiaoStudentByUserIdAndRaceId(Long race, Long userId) {

        RaceLanqiaoStudent lanqiaoStudent = (RaceLanqiaoStudent) selectRaceLanqiaoStudentByUserIdAndRaceId(race, userId);
        if (lanqiaoStudent != null) {
            deleteRaceLanqiaoStudentById(lanqiaoStudent.getId());
            //删除队伍
            teamInfoService.deleteTeamInfoByUserIdAndRaceId(race, userId);
            //删除预报名
            preSignStudentService.deletePreSignStudentByUserIdAndRaceId(race, userId);
        }
        return 1;
    }


    //    获取蓝桥杯学生报名信息详细信息
    @Override
    public RaceLanqiaoStudent selectRaceLanqiaoStudentByUserId(Long loginId) {
        RaceLanqiaoStudent raceLanqiaoStudent = raceLanqiaoStudentMapper.selectRaceLanqiaoStudentByUserId(loginId);
        return raceLanqiaoStudent;
    }

}
