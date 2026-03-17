package com.buis.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.buis.common.core.domain.AjaxResult;
import com.buis.common.exception.ServiceException;
import com.buis.common.utils.DateUtils;
import com.buis.model.domain.PreSignStudent;
import com.buis.model.enums.ReviewStatus;
import com.buis.mapper.PreSignStudentMapper;
import com.buis.model.vo.TrendResultVo;
import com.buis.service.IPreSignStudentService;
import com.buis.service.IRaceInfoService;
import com.buis.service.IUserInfoService;
import com.buis.model.vo.PreSignStudentVo;
import com.buis.model.vo.RaceInfoVo;
import com.buis.model.vo.UserInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    @Autowired
    private RedisTemplate redisTemplate;


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
//        preSignStudent.setRace();
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
    public List<PreSignStudentVo> selectPreSignStudentByRace(Long race) {
        return preSignStudentMapper.selectPreSignStudentByRace(race);
    }

    @Override
    public List<PreSignStudentVo> selectPreSignStudentByUserId(Long userId) {
        return preSignStudentMapper.selectPreSignStudentByUserId(userId);
    }

    @Override
    @Transactional
    public int studentSign(Long id, Long userId,PreSignStudent preSignStudent) {
        //参数校验
        if (id == null || userId == null) {
            throw new ServiceException("参数错误");
        }
        if (preSignStudent == null) {
            throw new ServiceException("报名信息不能为空!");
        }

        preSignStudent.setStudent(userId);
        //判断活动是否存在
        RaceInfoVo raceInfoVo = raceInfoService.selectRaceInfoById(id);
        if (raceInfoVo == null) {
            throw new ServiceException("活动不存在");
        }

        //判断是否重复报名
        if (this.selectByUserIdAndRace(userId, id) != null) {
            throw new ServiceException("您已经报名过该活动");
        }

        //判断活动是否已经发布
        if (!Objects.equals(raceInfoVo.getReviewStatus(), ReviewStatus.PUBLISH.getCode())) {
            throw new ServiceException("活动未发布");
        }

        preSignStudent.setRace(id);
        int ok = this.insertPreSignStudent(preSignStudent);
        if (ok > 0) {
            raceInfoService.deleteRaceInfoCacheByUserId(userId);
            return ok;
        } else {
            throw new ServiceException("报名失败");
        }
    }

    @Override
    public PreSignStudent getPreStudentsByRaceId(Long raceId) {
        //获取当前用户id
        Long userId = Long.valueOf(StpUtil.getLoginId().toString());
        //查询当前用户报名
        PreSignStudent preSignStudent = this.selectByUserIdAndRace(userId,raceId);
        PreSignStudentVo preSignStudentVo = new PreSignStudentVo();
        //查出用户信息
        UserInfoVo userInfoVo = userInfoService.selectUserInfoById(userId);
        RaceInfoVo raceInfoVo = raceInfoService.selectRaceInfoById(raceId);
        //插入
        BeanUtils.copyProperties(preSignStudent, preSignStudentVo);
//        preSignStudentVo.setStudentInfo(userInfoVo);
//        preSignStudentVo.setRaceName(raceInfoVo.getName());

//        return preSignStudentVo;
        return preSignStudent;
    }

    /**
     * 获取活动报名人数排行
     * @return
     */
    @Override
    public List<ActPeopleCountVo> getActPeopleCount() {
        return preSignStudentMapper.getActPeopleCount();
    }

    @Override
    public PreSignStudent selectByUserIdAndRace(Long userId, Long raceId) {
        return preSignStudentMapper.selectByUserIdAndRace(userId, raceId);
    }

    /**
     * 年度参与趋势分析
     */
    @Override
    public AjaxResult trend() {
        Object cached = redisTemplate.opsForValue().get("trend:data");
        if (cached != null) return AjaxResult.success(cached);

        List<TrendResultVo> data = preSignStudentMapper.selectMonthlyTrend();
        redisTemplate.opsForValue().set("trend:data", data);

        return AjaxResult.success(data);
    }

}
