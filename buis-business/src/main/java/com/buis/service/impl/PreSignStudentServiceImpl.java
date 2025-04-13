package com.buis.service.impl;

import com.buis.common.utils.DateUtils;
import com.buis.domain.PreSignStudent;
import com.buis.mapper.PreSignStudentMapper;
import com.buis.mapper.RaceInfoMapper;
import com.buis.mapper.UserInfoMapper;
import com.buis.service.IPreSignStudentService;
import com.buis.service.IRaceInfoService;
import com.buis.service.IUserInfoService;
import com.buis.vo.PreSignStudentVo;
import com.buis.vo.RaceInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 学生预报名Service业务层处理
 *
 * @author buis
 * @date 2024-10-11
 */
@Service
@Slf4j
public class PreSignStudentServiceImpl implements IPreSignStudentService {
    @Resource
    private PreSignStudentMapper preSignStudentMapper;
    @Resource
    private IUserInfoService userInfoService;
    @Resource
    private IRaceInfoService raceInfoService;


    /**
     * 查询学生预报名
     *
     * @param id 学生预报名主键
     * @return 学生预报名
     */
    @Override
    public PreSignStudentVo selectPreSignStudentById(Long id) {
        PreSignStudentVo preSignStudentVo = preSignStudentMapper.selectPreSignStudentById(id);
        setStudentInfo(preSignStudentVo);
        return preSignStudentVo;
    }

    /**
     * 查询学生预报名列表
     *
     * @param preSignStudent 学生预报名
     * @return 学生预报名列表
     */
    @Override
    public List<PreSignStudentVo> selectPreSignStudentList(PreSignStudent preSignStudent) {
        List<PreSignStudentVo> preSignStudents = preSignStudentMapper.selectPreSignStudentList(preSignStudent);
        for (PreSignStudentVo signStudent : preSignStudents) {
            setStudentInfo(signStudent);
        }
        return preSignStudents;
    }

    /**
     * 设置学生信息
     *
     * @param preSignStudentVo 学生预报名对象
     */
    private void setStudentInfo(PreSignStudentVo preSignStudentVo) {
        if (preSignStudentVo != null) {
            preSignStudentVo.setStudentInfo(userInfoService.selectUserInfoById(preSignStudentVo.getStudent()));
        }
    }


    /**
     * 新增学生预报名
     *
     * @param preSignStudent 学生预报名
     * @return 结果
     */
    @Override
    public int insertPreSignStudent(PreSignStudent preSignStudent) {
        preSignStudent.setCreateTime(DateUtils.getNowDate());
        return preSignStudentMapper.insertPreSignStudent(preSignStudent);
    }

    /**
     * 修改学生预报名
     *
     * @param preSignStudent 学生预报名
     * @return 结果
     */
    @Override
    public int updatePreSignStudent(PreSignStudent preSignStudent) {
        preSignStudent.setUpdateTime(DateUtils.getNowDate());
        return preSignStudentMapper.updatePreSignStudent(preSignStudent);
    }

    /**
     * 批量删除学生预报名
     *
     * @param ids 需要删除的学生预报名主键
     * @return 结果
     */
    @Override
    public int deletePreSignStudentByIds(Long[] ids) {
        return preSignStudentMapper.deletePreSignStudentByIds(ids);
    }

    /**
     * 删除学生预报名信息
     *
     * @param id 学生预报名主键
     * @return 结果
     */
    @Override
    public int deletePreSignStudentById(Long id) {
        return preSignStudentMapper.deletePreSignStudentById(id);
    }

    @Override
    public void deletePreSignStudentByRaceIdAndUserId(Long race, Long userId) {
        preSignStudentMapper.deletePreSignStudentByRaceIdAndUserId(race, userId);
    }

    @Override
    public List<PreSignStudentVo> fuzzyQuery(String name) {
        if (name == null) {
            name = "";
        }
        List<PreSignStudentVo> preSignStudentVos = preSignStudentMapper.fuzzyQuery(name);
        for (PreSignStudentVo preSignStudentVo : preSignStudentVos) {
            setStudentInfo(preSignStudentVo);
        }
        return preSignStudentVos;
    }

    @Override
    public PreSignStudent selectPreSignStudentByRace(Long race) {
        return preSignStudentMapper.selectPreSignStudentByRace(race);
    }

    @Override
    @Transactional
    public int studentSign(Long id, Long userId,PreSignStudent preSignStudent) {
        //参数校验
        if (id == null || userId == null) {
            throw new RuntimeException("参数错误");
        }
        if (preSignStudent == null) {
            throw new RuntimeException("报名信息不能为空!");
        }

        //判断活动是否存在
        RaceInfoVo raceInfoVo = raceInfoService.selectRaceInfoById(id);
        if (raceInfoVo == null) {
            throw new RuntimeException("活动不存在");
        }

        //判断活动是否已经发布
        if (raceInfoVo.getReviewStatus() != 3) {
            throw new RuntimeException("活动未发布");
        }

        int ok = this.insertPreSignStudent(preSignStudent);
        if (ok > 0) {
            return ok;
        } else {
            throw new RuntimeException("报名失败");
        }
    }

    @Override
    public PreSignStudent selectByUserIdAndRace(Long userId, Long raceId) {
        return preSignStudentMapper.selectByUserIdAndRace(userId, raceId);
    }

}
