package com.buis.service.impl;

import java.util.List;
import com.buis.common.utils.DateUtils;
import com.buis.service.IRaceInfoService;
import com.buis.service.IUserInfoService;
import com.buis.vo.AssistantVo;
import com.buis.vo.TeamMemberTeacherVo;
import com.buis.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.buis.mapper.AssistantMapper;
import com.buis.domain.Assistant;
import com.buis.service.IAssistantService;

import javax.annotation.Resource;

/**
 * 学生助理Service业务层处理
 * 
 * @author buis
 * @date 2024-10-11
 */
@Service
public class AssistantServiceImpl implements IAssistantService 
{
    @Resource
    private AssistantMapper assistantMapper;
    @Resource
    private IUserInfoService userInfoService;
    @Resource
    private IRaceInfoService raceInfoService;

    /**
     * 查询学生助理
     * 
     * @param id 学生助理主键
     * @return 学生助理
     */
    @Override
    public Assistant selectAssistantById(Long id)
    {
        return assistantMapper.selectAssistantById(id);
    }

    /**
     * 查询学生助理列表
     * 
     * @param assistant 学生助理
     * @return 学生助理
     */
    @Override
    public List<AssistantVo> selectAssistantList(Assistant assistant)
    {
        List<AssistantVo> assistantVos = assistantMapper.selectAssistantList(assistant);
        for (AssistantVo assistantVo : assistantVos) {
            setUserInfo(assistantVo);
            setRaceInfo(assistantVo);
        }
        return assistantVos;
    }

    /**
     * 新增学生助理
     * 
     * @param assistant 学生助理
     * @return 结果
     */
    @Override
    public int insertAssistant(Assistant assistant)
    {
        assistant.setCreateTime(DateUtils.getNowDate());
        return assistantMapper.insertAssistant(assistant);
    }

    /**
     * 修改学生助理
     * 
     * @param assistant 学生助理
     * @return 结果
     */
    @Override
    public int updateAssistant(Assistant assistant)
    {
        assistant.setUpdateTime(DateUtils.getNowDate());
        return assistantMapper.updateAssistant(assistant);
    }

    /**
     * 批量删除学生助理
     * 
     * @param ids 需要删除的学生助理主键
     * @return 结果
     */
    @Override
    public int deleteAssistantByIds(Long[] ids)
    {
        return assistantMapper.deleteAssistantByIds(ids);
    }

    /**
     * 删除学生助理信息
     * 
     * @param id 学生助理主键
     * @return 结果
     */
    @Override
    public int deleteAssistantById(Long id)
    {
        return assistantMapper.deleteAssistantById(id);
    }

    /**
     * 联表模糊查询学生助理列表
     *
     * @param name
     * @return
     */
    @Override
    public List<AssistantVo> fuzzyQuery(String name) {
        if (name == null) {
            name = "";
        }
        List<AssistantVo> assistantVos = assistantMapper.fuzzyQuery(name);
        for (AssistantVo assistantVo : assistantVos) {
            setUserInfo(assistantVo);
            setRaceInfo(assistantVo);
        }
        return assistantVos;
    }

    @Override
    public List<UserInfoVo> selectAssistantListByRace(Long id) {
        return assistantMapper.selectAssistantListByRace(id);
    }

    /**
     * 设置学生信息
     *
     * @param assistantVo 学生助理管理对象
     */
    private void setUserInfo(AssistantVo assistantVo) {
        if (assistantVo != null) {
            assistantVo.setUserInfo(userInfoService.selectUserInfoById(assistantVo.getStudent()));
        }
    }

    /**
     * 设置学生信息
     *
     * @param assistantVo 学生助理管理对象
     */
    private void setRaceInfo(AssistantVo assistantVo) {
        if (assistantVo != null) {
            assistantVo.setRaceInfo(raceInfoService.selectRaceInfoById(assistantVo.getRace()));
        }
    }
}
