package com.buis.service.impl;

import java.util.ArrayList;
import java.util.List;
import com.buis.common.utils.DateUtils;
import com.buis.common.utils.bean.BeanUtils;
import com.buis.domain.TeamMemberTeacher;
import com.buis.mapper.TeamMemberTeacherMapper;
import com.buis.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.buis.mapper.UserInfoMapper;
import com.buis.domain.UserInfo;
import com.buis.service.IUserInfoService;

/**
 * 用户信息Service业务层处理
 * 
 * @author buis
 * @date 2024-10-11
 */
@Service
public class UserInfoServiceImpl implements IUserInfoService 
{
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private TeamMemberTeacherMapper teamMemberTeacherMapper;
    /**
     * 查询用户信息
     * 
     * @param id 用户信息主键
     * @return 用户信息
     */
    @Override
    public UserInfoVo selectUserInfoById(Long id)
    {
        UserInfoVo vo = new UserInfoVo();
        UserInfo userInfo = userInfoMapper.selectUserInfoById(id);
        BeanUtils.copyProperties(userInfo,vo);
        if (vo.getTitle() != null) {
            TeamMemberTeacher teamMemberTeacher = new TeamMemberTeacher();
            teamMemberTeacher.setTeacher(id);
            teamMemberTeacher.setIsChoosed(1L);
            List<TeamMemberTeacher> teamMemberTeachers = teamMemberTeacherMapper.selectTeamMemberTeacherList(teamMemberTeacher);
            long teamNum = teamMemberTeachers.stream().count();
            vo.setTeamNum(teamNum);
            vo.setIsTeacher(true);
        }
        return vo;
    }

    /**
     * 查询用户信息列表
     * 
     * @param userInfo 用户信息
     * @return 用户信息
     */
    @Override
    public List<UserInfoVo> selectUserInfoList(UserInfo userInfo)
    {
        List<UserInfoVo> userInfoVos = new ArrayList<>();
        UserInfoVo vo = new UserInfoVo();
        List<UserInfo> userInfos = userInfoMapper.selectUserInfoList(userInfo);
        for (UserInfo user : userInfos) {
            BeanUtils.copyProperties(user,vo);
            if (vo.getTitle() != null) {
                TeamMemberTeacher teamMemberTeacher = new TeamMemberTeacher();
                teamMemberTeacher.setTeacher(userInfo.getId());
                teamMemberTeacher.setIsChoosed(1L);
                List<TeamMemberTeacher> teamMemberTeachers = teamMemberTeacherMapper.selectTeamMemberTeacherList(teamMemberTeacher);
                long teamNum = teamMemberTeachers.stream().count();
                vo.setTeamNum(teamNum);
                vo.setIsTeacher(true);
            }
            userInfoVos.add(vo);
        }
        return userInfoVos;
    }

    /**
     * 新增用户信息
     * 
     * @param userInfo 用户信息
     * @return 结果
     */
    @Override
    public int insertUserInfo(UserInfo userInfo)
    {
        userInfo.setCreateTime(DateUtils.getNowDate());
        return userInfoMapper.insertUserInfo(userInfo);
    }

    /**
     * 修改用户信息
     * 
     * @param userInfo 用户信息
     * @return 结果
     */
    @Override
    public int updateUserInfo(UserInfo userInfo)
    {
        userInfo.setUpdateTime(DateUtils.getNowDate());
        return userInfoMapper.updateUserInfo(userInfo);
    }

    /**
     * 批量删除用户信息
     * 
     * @param ids 需要删除的用户信息主键
     * @return 结果
     */
    @Override
    public int deleteUserInfoByIds(Long[] ids)
    {
        return userInfoMapper.deleteUserInfoByIds(ids);
    }

    /**
     * 删除用户信息信息
     * 
     * @param id 用户信息主键
     * @return 结果
     */
    @Override
    public int deleteUserInfoById(Long id)
    {
        return userInfoMapper.deleteUserInfoById(id);
    }
}
