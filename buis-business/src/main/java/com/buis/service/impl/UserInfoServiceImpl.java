package com.buis.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.buis.common.constant.RedisConstant;
import com.buis.common.core.domain.R;
import com.buis.common.exception.ServiceException;
import com.buis.common.utils.DateUtils;
import com.buis.common.utils.FormatUtils;
import com.buis.common.utils.StringUtils;
import com.buis.common.utils.bean.BeanUtils;
import com.buis.domain.UserInfo;
import com.buis.dto.RegisterDto;
import com.buis.dto.UserLoginDto;
import com.buis.mapper.UserInfoMapper;
import com.buis.service.ITeamMemberTeacherService;
import com.buis.service.IUserInfoService;
import com.buis.system.service.ISysDictDataService;
import com.buis.vo.UserInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    private ISysDictDataService sysDictDataService;
    @Resource
    private ITeamMemberTeacherService teamMemberTeacherService;
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
        UserInfo userInfo = userInfoMapper.selectUserInfoById(id);
        return convertToUserInfoVo(userInfo);
    }

    /**
     * 根据用户ID列表查询用户信息
     *
     * @param ids 用户信息主键列表
     * @return 用户信息列表
     */
    @Override
    public List<UserInfo> selectUserInfoByIds(List<Long> ids) {
//        List<UserInfoVo> userInfoVos = new ArrayList<>();
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return userInfoMapper.selectUserInfoByIds(ids);
//        for (UserInfo user : userInfos) {
//            if (user != null) {
//                UserInfoVo vo = convertToUserInfoVo(user);
//                userInfoVos.add(vo);
//            }
//        }

    }

    @Override
    public List<UserInfoVo> selectUserInfoVoByIds(List<Long> ids) {
        return userInfoMapper.selectUserInfoByIds2(ids);
    }

    /**
     * 查询用户信息列表
     *
     * @param userInfo 用户信息
     * @return 用户信息列表
     */
    @Override
    public List<UserInfoVo> selectUserInfoList(UserInfo userInfo) {
        List<UserInfoVo> userInfoVos = new ArrayList<>();
        List<UserInfo> userInfos = userInfoMapper.selectUserInfoList(userInfo);
        for (UserInfo user : userInfos) {
            if (user != null) {
                UserInfoVo vo = convertToUserInfoVo(user);
                userInfoVos.add(vo);
            }
        }
        return userInfoVos;
    }

    /**
     * 将 UserInfo 转换为 UserInfoVo
     *
     * @param userInfo 用户信息
     * @return 用户信息视图对象
     */
    private UserInfoVo convertToUserInfoVo(UserInfo userInfo) {
        UserInfoVo vo = new UserInfoVo();
        if (userInfo != null) {
            BeanUtils.copyProperties(userInfo, vo);
            //把年级,班级插入
            //根据grade查询到字典对应的值
            int grade = userInfo.getGrade();
            String sClass = userInfo.getSClass();
            String stu_grade = sysDictDataService.selectDictLabel("stu_grade", String.valueOf(grade));
            vo.setStuGrade(stu_grade);
            String stu_class = sysDictDataService.selectDictLabel("stu_class", sClass);
            vo.setStuClass(stu_class);
            if (vo.getIsTeacher() != null && vo.getIsTeacher() == 1) { // 判断是否为老师
                //是老师
                Long count = teamMemberTeacherService.getGuideCount(userInfo.getId());
                vo.setTeamNum(count);
            }
        }
        return vo;
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


    /**
     * 用户注册
     * @param registerDTO
     * @return
     */
    @Override
    @Transactional
    public int register(RegisterDto registerDTO) {

        //获取注册信息，进行校验
        String email = registerDTO.getEmail();
        String code = registerDTO.getCode();
        String password = registerDTO.getPassword();
        String name = registerDTO.getName();
        String number = registerDTO.getNumber();
        Integer isTeacher = registerDTO.getIsTeacher();

        //校验邮箱格式
        //邮箱的正则表达式
       FormatUtils.checkEmailFormat(email);

        //校验参数
        if (StringUtils.isAnyEmpty(code, password, name, email)) {
            throw new ServiceException("请完善注册内容~");
        }

        //校验校验验证码
        //从redis获取发送的验证码
        String rCode = (String) redisTemplate.opsForValue().get(RedisConstant.EMAIL + email);
        if (rCode == null) {
            throw new ServiceException("验证码已过期,请重新获取!");
        }
        if (!rCode.equals(code)) {
            throw new ServiceException("验证码错误,请重新输入!");
        }

        //查询数据库中是否存在相同的邮箱号码 和 相同的学号
        //有的话说明该用户已存在
        UserInfo userInfoByEmail = userInfoMapper.getLoginInfoByEmail(email);
        UserInfo userInfoByNumber = userInfoMapper.getLoginInfoByNumber(number);
        if (userInfoByNumber != null) {
            throw new ServiceException("学号为:" + number + "的用户已存在!");
        }
        if (userInfoByEmail != null) {
            throw new ServiceException("邮箱为:" + email + "的用户已存在!");
        }

        //密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());

        //添加注册信息到数据库
        UserInfo member = new UserInfo();
        member.setName(name);
        member.setEmail(email);
        member.setNumber(number);
        member.setPassword(encryptPassword);
        member.setIsTeacher(isTeacher);
        member.setIsDisabled(0);
        return userInfoMapper.insertUserInfo(member);
    }

    /**
     * 获取当前登录用户
     *
     * @return
     */
    @Override
    public UserInfo getLoginUser() {
        //获取当前用户id
//        Long loginId = (Long) StpUtil.getLoginId();
        String loginIdStr = StpUtil.getLoginId().toString();
        Long loginId;
        try {
            loginId = Long.parseLong(loginIdStr);
        } catch (NumberFormatException e) {
            throw new ServiceException("无效的用户ID格式");
        }
        UserInfo userInfo = userInfoMapper.selectUserInfoById(loginId);
        if (userInfo == null) {
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
    public String userLogin(UserLoginDto userLoginDTO, HttpServletRequest request) {
        String number = userLoginDTO.getNumber();
        String email = userLoginDTO.getEmail();
        String passWord = userLoginDTO.getPassWord();
        String code = userLoginDTO.getCode();
        Integer isTeacher = userLoginDTO.getIsTeacher();
        //1.对参数做校验
        if (StringUtils.isEmpty(email) && StringUtils.isEmpty(number)) {
            throw new ServiceException("账号不能为空!");
        }
        UserInfo userInfo = null;
        if (StringUtils.isNotEmpty(email)) {
            //邮箱登录
            //查询用户是否存在
            //根据用户的邮箱查询用户
            userInfo = userInfoMapper.getLoginInfoByEmail(email);
            //用户不存在
            if (userInfo == null) {
                log.info("user login fail,userAccount cannot match userPassword");
                throw new ServiceException("用户不存在,请先注册!");
            }
        } else if (StringUtils.isNotEmpty(number)) {
            //学号登录
            //查询用户是否存在
            //根据用户的学号查询用户
            userInfo = userInfoMapper.getLoginInfoByNumber(number);
            //用户不存在
            if (userInfo == null) {
                log.info("user login fail,userAccount cannot match userPassword");
                throw new ServiceException("用户不存在,请先注册!");
            }
        }
        Integer isTeacher1 = null;
        if (userInfo != null) {
            isTeacher1 = userInfo.getIsTeacher();
        }

        //判断isTeacher是否对应
        if (isTeacher != null && !isTeacher.equals(isTeacher1)) {
            if(isTeacher==0){
            throw new ServiceException("您还未注册比赛成员~");
            }else if (isTeacher==1){
                throw new ServiceException("您还未注册比赛指导老师~");
            }
        }

        //2.判断是密码还是验证码登录
        if (!StringUtils.isEmpty(passWord)) {
            // 如果密码不为空则是密码登录
            //把输入的密码加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + passWord).getBytes());
            if (!encryptPassword.equals(userInfo.getPassword())) {
                throw new ServiceException("用户密码错误,请重新输入~");
            }
        } else if (!StringUtils.isEmpty(code)) {
            //从缓存中取到code
            String rcode = (String) redisTemplate.opsForValue().get(RedisConstant.EMAIL + email);
            if (!rcode.equals(code)) {
                throw new ServiceException("验证码错误~");
            }
        } else {
            throw new ServiceException("账号或密码不能为空!");
        }

        //3.校验账户
        //邮箱的正则表达式
        if (StringUtils.isNotEmpty(email)) {
            FormatUtils.checkEmailFormat(email);
        }

        assert userInfo != null;
        StpUtil.login(userInfo.getId());
        return StpUtil.getTokenValue();

    }

    @Override
    public boolean isSelf(Long loginUserId, Long userId) {
        return loginUserId != null && !loginUserId.equals(userId);
    }

    @Override
    public UserInfo selectUserInfoByEmail(String email) {
        if (StringUtils.isEmpty(email)){
            throw new ServiceException("邮箱不能为空!");
        }
        UserInfo userInfo = userInfoMapper.getLoginInfoByEmail(email);
        if (userInfo==null){
            throw new ServiceException("用户不存在!");
        }
        return userInfo;
    }

    @Override
    public UserInfo selectUserInfoBynumber(String number) {
        if (StringUtils.isEmpty(number)){
            throw new ServiceException("学号不能为空!");
        }
        return userInfoMapper.selectUserInfoBynumber(number);
    }

    @Override
    public UserInfoVo selectUserInfoByName(String name) {
        if (StringUtils.isEmpty(name)){
            throw new ServiceException("用户名不能为空!");
        }
        return userInfoMapper.selectUserInfoByName(name);
    }

    @Override
    public List<UserInfoVo> selectAllTeachers(Integer isTeacher) {
        return userInfoMapper.selectTeachers(isTeacher);
    }

    @Override
    public List<UserInfoVo> queryAssistantsByPage(Integer pageNum, Integer pageSize) {
        //跳过前面(pageNum-1)*pageSize数据
        int offset =(pageNum-1)*pageSize;
        //查询当前页学生助理
        List<UserInfoVo> assistants =userInfoMapper.selectAssistants(pageSize,offset);
        return assistants;
    }

    @Override
    public R updateAssistantStatus(Long userId, Integer isAssistant) {
        UserInfo user=userInfoMapper.selectUserInfoById(userId);
        if (user==null){
            return R.fail("用户不存在");
        }

        //判断是否为老师
        if (user.getIsTeacher()==1){
           return R.fail("用户不是学生");
        }

        int rows =userInfoMapper.updateAssistantStatus(userId,isAssistant);
        if (rows!=1){
           return R.fail("设置失败");
        }

        return R.ok("设置成功");

    }


}
