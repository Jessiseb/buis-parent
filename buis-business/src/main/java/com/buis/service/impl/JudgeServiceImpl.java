package com.buis.service.impl;

import java.util.List;
import com.buis.common.utils.DateUtils;
import com.buis.service.IRaceInfoService;
import com.buis.service.IUserInfoService;
import com.buis.model.vo.JudgeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.buis.mapper.JudgeMapper;
import com.buis.model.domain.Judge;
import com.buis.service.IJudgeService;

import javax.annotation.Resource;

/**
 * 评委管理Service业务层处理
 * 
 * @author buis
 * @date 2024-10-10
 */
@Service
public class JudgeServiceImpl implements IJudgeService 
{
    @Autowired
    private JudgeMapper judgeMapper;
    @Resource
    private IUserInfoService userInfoService;
    @Resource
    private IRaceInfoService raceInfoService;

    /**
     * 查询评委管理
     * 
     * @param id 评委管理主键
     * @return 评委管理
     */
    @Override
    public Judge selectJudgeById(Long id)
    {
        return judgeMapper.selectJudgeById(id);
    }

    /**
     * 查询评委管理列表
     * 
     * @param judge 评委管理
     * @return 评委管理
     */
    @Override
    public List<JudgeVo> selectJudgeList(Judge judge)
    {
        List<JudgeVo> judgeVos = judgeMapper.selectJudgeList(judge);
        for (JudgeVo judgeVo : judgeVos) {
            setUserInfo(judgeVo);
            setRaceInfo(judgeVo);
        }
        return judgeVos;
    }

    /**
     * 新增评委管理
     * 
     * @param judge 评委管理
     * @return 结果
     */
    @Override
    public int insertJudge(Judge judge)
    {
        judge.setCreateTime(DateUtils.getNowDate());
        return judgeMapper.insertJudge(judge);
    }

    /**
     * 修改评委管理
     * 
     * @param judge 评委管理
     * @return 结果
     */
    @Override
    public int updateJudge(Judge judge)
    {
        judge.setUpdateTime(DateUtils.getNowDate());
        return judgeMapper.updateJudge(judge);
    }

    /**
     * 批量删除评委管理
     * 
     * @param ids 需要删除的评委管理主键
     * @return 结果
     */
    @Override
    public int deleteJudgeByIds(Long[] ids)
    {
        return judgeMapper.deleteJudgeByIds(ids);
    }

    /**
     * 删除评委管理信息
     * 
     * @param id 评委管理主键
     * @return 结果
     */
    @Override
    public int deleteJudgeById(Long id)
    {
        return judgeMapper.deleteJudgeById(id);
    }

    /**
     * 联表模糊查询评委列表
     *
     * @param name
     */
    @Override
    public List<JudgeVo> fuzzyQuery(String name) {
        if (name == null) {
            name = "";
        }
        List<JudgeVo> judgeVos = judgeMapper.fuzzyQuery(name);
        for (JudgeVo judgeVo : judgeVos) {
            setUserInfo(judgeVo);
            setRaceInfo(judgeVo);
        }
        return judgeVos;
    }

    /**
     * 根据教师ID查询评委信息
     */
    @Override
    public Judge selectJudgeByTeacherId(Long teacherId) {
       return judgeMapper.selectJudgeByTeacherId(teacherId);
    }

    /**
     * 设置评委信息
     *
     * @param judgeVo 评委管理对象
     */
    private void setUserInfo(JudgeVo judgeVo) {
        if (judgeVo != null) {
            judgeVo.setUserInfo(userInfoService.selectUserInfoById(judgeVo.getTeacher()));
        }
    }

    /**
     * 设置学生信息
     *
     * @param judgeVo 评委管理对象
     */
    private void setRaceInfo(JudgeVo judgeVo) {
        if (judgeVo != null) {
            judgeVo.setRaceInfo(raceInfoService.selectRaceInfoById(judgeVo.getRace()));
        }
    }
}
