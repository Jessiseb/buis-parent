package com.ruoyi.businessmanager.userinfo.service.impl;

import java.util.List;

import com.ruoyi.businessmanager.userinfo.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.businessmanager.userinfo.mapper.UserInfoMapper;
import com.ruoyi.businessmanager.userinfo.domain.entities.UserInfo;
import com.ruoyi.businessmanager.userinfo.service.IUserInfoService;

/**
 * 用户信息管理Service业务层处理
 * 
 * @author zjx
 * @date 2024-10-01
 */
@Service
public class UserInfoServiceImpl implements IUserInfoService 
{
    @Autowired
    private UserInfoMapper userInfoMapper;

    /**
     * 查询用户信息管理
     * 
     * @param id 用户信息管理主键
     * @return 用户信息管理
     */
    @Override
    public UserInfo selectUserInfoById(Long id)
    {
        return userInfoMapper.selectUserInfoById(id);
    }

    /**
     * 查询用户信息管理列表
     * 
     * @param userInfo 用户信息管理
     * @return 用户信息管理
     */
    @Override
    public List<UserInfo> selectUserInfoList(UserInfo userInfo)
    {
        return userInfoMapper.selectUserInfoList(userInfo);
    }

    /**
     * 新增用户信息管理
     * 
     * @param userInfo 用户信息管理
     * @return 结果
     */
    @Override
    public int insertUserInfo(UserInfo userInfo)
    {
        return userInfoMapper.insertUserInfo(userInfo);
    }

    /**
     * 修改用户信息管理
     * 
     * @param userInfo 用户信息管理
     * @return 结果
     */
    @Override
    public int updateUserInfo(UserInfo userInfo)
    {
        return userInfoMapper.updateUserInfo(userInfo);
    }

    /**
     * 批量删除用户信息管理
     * 
     * @param ids 需要删除的用户信息管理主键
     * @return 结果
     */
    @Override
    public int deleteUserInfoByIds(Long[] ids)
    {
        return userInfoMapper.deleteUserInfoByIds(ids);
    }

    /**
     * 删除用户信息管理信息
     * 
     * @param id 用户信息管理主键
     * @return 结果
     */
    @Override
    public int deleteUserInfoById(Long id)
    {
        return userInfoMapper.deleteUserInfoById(id);
    }
}
