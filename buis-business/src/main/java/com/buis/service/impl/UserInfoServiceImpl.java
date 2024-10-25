package com.buis.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import com.buis.common.constant.RedisConstant;
import com.buis.common.core.domain.R;
import com.buis.common.exception.ServiceException;
import com.buis.common.utils.DateUtils;
import com.buis.common.utils.StringUtils;
import com.buis.common.utils.bean.BeanUtils;
import com.buis.domain.TeamMemberTeacher;
import com.buis.domain.UserInfo;
import com.buis.dto.RegisterDTO;
import com.buis.dto.UserLoginDTO;
import com.buis.mapper.TeamMemberTeacherMapper;
import com.buis.mapper.UserInfoMapper;
import com.buis.service.IUserInfoService;
import com.buis.vo.UserInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.buis.common.constant.UserConstants.SYS_USER;
import static com.buis.common.constant.UserConstants.USER_LOGIN_STATE;

/**
 * 用户信息Service业务层处理
 *
 * @author buis
 * @date 2024-10-11
 */
@Service
@Slf4j
public class UserInfoServiceImpl implements IUserInfoService {
    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private TeamMemberTeacherMapper teamMemberTeacherMapper;

    @Resource
    private RedisTemplate redisTemplate;

    public static final String SALT = "salt";

    /**
     * 查询用户信息
     *
     * @param id 用户信息主键
     * @return 用户信息
     */
    @Override
    public UserInfoVo selectUserInfoById(Long id) {
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
        } else {
            vo.setIsTeacher(false);
            vo.setTeamNum(0L);
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
            } else {
                vo.setIsTeacher(false);
                vo.setTeamNum(0L);
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
    public int insertUserInfo(UserInfo userInfo) {
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
    public int updateUserInfo(UserInfo userInfo) {
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
    public int deleteUserInfoByIds(Long[] ids) {
        return userInfoMapper.deleteUserInfoByIds(ids);
    }

    /**
     * 删除用户信息信息
     *
     * @param id 用户信息主键
     * @return 结果
     */
    @Override
    public int deleteUserInfoById(Long id) {
        return userInfoMapper.deleteUserInfoById(id);
    }

//    /**
//     * 前台用户登录
//     *
//     * @return
//     */
//    @Override
//    public boolean login(UserLoginDTO loginDTO) {
//        //判空
//        if (null == loginDTO) {
//            throw new ServiceException("参数不能为空!", 50001);
//        }
//        String email = loginDTO.getEmail();
//        String passWord = loginDTO.getPassWord();
//        //查询用户是否存在
//        UserInfoVo user = this.selectUserInfoById(id);
//        if (user == null) {
//            throw new ServiceException("用户不存在,请先注册!", 50001);
//        }
//        //参数校验
//        if (StringUtils.isEmpty(email)) {
//            throw new ServiceException("邮箱不能为空!!", 50001);
//        }
//        if (StringUtils.isEmpty(passWord)) {
//            throw new ServiceException("密码不能为空!!", 50001);
//        }
//        //校验是否被禁用
//        if (user.getIsDeleted().equals("1")) {
//            throw new ServiceException("该用户已被禁用,请联系管理员!!", 50001);
//        }
//        StpUtil.login(loginDTO.getId());
//        return true;
//    }

    @Override
    public int register(RegisterDTO registerDTO) {
        //TODO 封装错误信息
        //获取注册信息，进行校验
        String name = registerDTO.getName();
        String email = registerDTO.getEmail();
        String password = registerDTO.getPassword();
        //TODO 校验验证码
        String code = registerDTO.getCode();
        //校验参数
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            throw new ServiceException();
        }
        //校验校验验证码
        //从redis获取发送的验证码
        //TODO
//        String mobleCode = (String) redisTemplate.opsForValue().get(RedisConstant.EMAIL + email);
//        if (!code.equals(mobleCode)) {
//            throw new ServiceException();
//        }
        //查询数据库中是否存在相同的邮箱号码
//        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("email", email);
//        Long count = baseMapper.selectCount(queryWrapper);
        int count = userInfoMapper.selectCount(email);
        if (count > 0) {
            throw new ServiceException("用户已存在!");
        }
        //添加注册信息到数据库
        UserInfo member = new UserInfo();
        member.setName(name);
        member.setEmail(registerDTO.getEmail());
        member.setPassword(SaSecureUtil.md5(password));
        member.setId(registerDTO.getId());
        member.setNumber(registerDTO.getNumber());
//        member.setAvatar(UserConstant.DEFAULT_AVATAR);
       return userInfoMapper.register(member);
    }

    /**
     * 获取当前登录用户
     * @return
     */
    @Override
    public UserInfo getLoginUser() {
        //获取当前用户id
        Long loginId = (Long) StpUtil.getLoginId();
        UserInfo userInfo = userInfoMapper.selectUserInfoById(loginId);
        if (userInfo == null){
            throw new ServiceException("用户不存在!");
        }
        return userInfo;
    }


    @Override
    public UserInfo getLoginInfoByUserId(Long id) {
        if (id != null) {
            return userInfoMapper.getLoginInfoByUserId(id);
        }
        throw new ServiceException("参数错误!");
    }

    @Override
    public UserInfoVo userLogin(UserLoginDTO userLoginDTO, HttpServletRequest request) {
        String email = userLoginDTO.getEmail();
        String passWord = userLoginDTO.getPassWord();
        String code = userLoginDTO.getCode();
        //1.对参数做校验
        if (StringUtils.isEmpty(email)) {
            throw new ServiceException("邮箱账号不能为空!");
        }

        //2.判断是密码还是验证码登录
        if (!StringUtils.isEmpty(passWord)){
           // 如果密码不为空则是密码登录
            //把输入的密码加密
            String encryptPassword = SaSecureUtil.md5(SALT + passWord);
        }else if (!StringUtils.isEmpty(code)){
            //从缓存中取到code
            String rcode = (String) redisTemplate.opsForValue().get(RedisConstant.EMAIL + email);
        }

        //3.校验账户
        //邮箱的正则表达式
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Matcher matcher = Pattern.compile(emailRegex).matcher(email);
        if (!matcher.find()) {
            throw new ServiceException("邮箱格式有误,请重新输入!");
        }

        //4.查询用户是否存在
        //根据用户的邮箱查询用户
        UserInfo userInfo = userInfoMapper.selectUserInfoByEmail(email);
        //用户不存在
        if (userInfo == null){
            log.info("user login fail,userAccount cannot match userPassword");
            throw new ServiceException("用户不存在,请先注册!");
        }

        //用户脱敏
        UserInfoVo safetyUser = new UserInfoVo();
        safetyUser.setId(userInfo.getId());
        safetyUser.setNumber(userInfo.getNumber());
        safetyUser.setEmail(userInfo.getEmail());
        safetyUser.setPhone(userInfo.getPhone());
        safetyUser.setName(userInfo.getName());
        safetyUser.setDescription(userInfo.getDescription());
        safetyUser.setSex(userInfo.getSex());
        safetyUser.setCollege(userInfo.getCollege());
        safetyUser.setMajor(userInfo.getMajor());
        safetyUser.setTitle(userInfo.getTitle());

        //5.记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        StpUtil.login(userInfo.getId());

        return safetyUser;




    }
}
