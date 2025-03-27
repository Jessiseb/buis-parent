package com.buis.service;

import com.buis.domain.UserInfo;
import com.buis.dto.RegisterDto;
import com.buis.dto.UserLoginDto;
import com.buis.vo.UserInfoVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户信息Service接口
 *
 * @author buis
 * @date 2024-10-11
 */
public interface IUserInfoService {
    /**
     * 查询用户信息
     *
     * @param id 用户信息主键
     * @return 用户信息
     */
    public UserInfoVo selectUserInfoById(Long id);
    /**
     * 根据用户ID列表查询用户信息
     *
     * @param ids 用户信息主键列表
     * @return 用户信息列表
     */
    List<UserInfoVo> selectUserInfoByIds(List<Long> ids);

    /**
     * 查询用户信息列表
     *
     * @param userInfo 用户信息
     * @return 用户信息集合
     */
    public List<UserInfoVo> selectUserInfoList(UserInfo userInfo);

    /**
     * 新增用户信息
     *
     * @param userInfo 用户信息
     * @return 结果
     */
    public int insertUserInfo(UserInfo userInfo);

    /**
     * 修改用户信息
     *
     * @param userInfo 用户信息
     * @return 结果
     */
    public int updateUserInfo(UserInfo userInfo);

    /**
     * 批量删除用户信息
     *
     * @param ids 需要删除的用户信息主键集合
     * @return 结果
     */
    public int deleteUserInfoByIds(Long[] ids);

    /**
     * 删除用户信息信息
     *
     * @param id 用户信息主键
     * @return 结果
     */
    public int deleteUserInfoById(Long id);

    int register(RegisterDto registerDTO);

    UserInfo getLoginUser();


    UserInfo getLoginInfoByUserId(Long id);

    /**
     * 用户登录接口
     * @param  userLoginDTO 登录用户信息
     * @param request session
     * @return 结果
     */
    String userLogin(UserLoginDto userLoginDTO, HttpServletRequest request);


    /**
     * 抽取出判断是否为本人修改的接口
     *
     * @param loginUserId 登录用户id
     * @param userId 被修改的用户id
     */
    boolean isSelf(Long loginUserId,Long userId);


    UserInfo selectUserInfoByEmail(String email);

    /**
     * 根据学号查询用户信息
     */
    UserInfo selectUserInfoBynumber(String number);

    /**
     * 根据名字搜索用户信息
     * @param name
     * @return
     */
    UserInfoVo selectUserInfoByName(String name);

    List<UserInfoVo> selectAllTeachers(Integer isTeacher);

}
