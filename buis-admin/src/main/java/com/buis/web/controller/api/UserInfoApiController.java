package com.buis.web.controller.api;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import com.buis.common.constant.RedisConstant;
import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.R;
import com.buis.common.core.page.MyPageVo;
import com.buis.common.exception.ServiceException;
import com.buis.common.utils.DateUtils;
import com.buis.common.utils.SecurityUtils;
import com.buis.common.utils.StringUtils;
import com.buis.common.utils.alioss.AliOssUtil;
import com.buis.model.domain.AwardRecords;
import com.buis.model.domain.CollegeMajor;
import com.buis.model.domain.Notice;
import com.buis.model.domain.UserInfo;
import com.buis.model.dto.RegisterDto;
import com.buis.model.dto.UserLoginDto;
import com.buis.service.ICollegeMajorService;
import com.buis.service.INoticeService;
import com.buis.service.IUserInfoService;
import com.buis.service.impl.AwardRecordsServiceImpl;
import com.buis.service.impl.AwardServiceImpl;
import com.buis.model.vo.AwardVo;
import com.buis.model.vo.FindUserVo;
import com.buis.model.vo.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.rmi.ServerException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.buis.service.impl.UserInfoServiceImpl.SALT;

@Api(tags = "用户信息Api")
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserInfoApiController extends BaseController {
    @Resource
    private IUserInfoService userInfoService;
    @Autowired
    private ICollegeMajorService collegeMajorService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private AwardServiceImpl awardService;
    @Resource
    private AwardRecordsServiceImpl awardRecordsService;
    @Autowired
    private AliOssUtil aliOssUtil;
    @Resource
    private INoticeService noticeService;


    @ApiOperation("用户登录功能")
    @PostMapping("/login")
    public R<String> login(@ApiParam(name = "userLoginDTO", value = "用户登录信息", required = true)@RequestBody UserLoginDto userLoginDTO, HttpServletRequest request) {
        if (userLoginDTO == null) {
            throw new ServiceException("参数有误!");
        }
        String token = userInfoService.userLogin(userLoginDTO, request);
        return R.ok(token);
    }


    @ApiOperation("用户注册功能")
    @PostMapping("/register")
    public R<Long> register(@ApiParam(name = "registerDTO", value = "用户注册信息", required = true)
                            @RequestBody RegisterDto registerDTO) {
        //判空
        if (registerDTO == null) {
            throw new ServiceException("参数错误!");
        }
        long result = userInfoService.register(registerDTO);
        //返回注册id
        return R.ok(result);
    }

    @ApiOperation("用户退出登录功能")
    @PostMapping("/logout")
    public R logout() {
        StpUtil.logout();
        return R.ok(null,"退出成功!");
    }


    /**
     * 查询用户信息
     * @return
     */
    @ApiOperation("查询用户信息")
    @PostMapping("/getUserInfo")
    public R<UserInfoVo> getUserInfo(){
        Long userId = SecurityUtils.getFrontLoginUserId();
        return R.ok(userInfoService.selectUserInfoById(userId));
    }

    /**
     * 根据学号查询用户信息
     */
    @ApiOperation("根据学号查询用户信息")
    @PostMapping("/getUserInfoByNumber")
    public R<UserInfoVo> getUserInfoByNumber(@RequestParam("number") String number){
        UserInfo userInfo = userInfoService.selectUserInfoBynumber(number);
        if (userInfo==null){
            throw new ServiceException("用户不存在!");
        }
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtil.copyProperties(userInfo,userInfoVo);
        return R.ok(userInfoVo);
    }

    /**
     * 根据奖状id删除对应的奖状信息
     *
     */
    @ApiOperation("根据奖状id删除对应的奖状信息")
    @PostMapping("/deletedAwardById/{id}")
    public R<Integer> deletedAwardById(@PathVariable("id") Long id){
       //根据id查询到对应的奖状
        AwardVo awardVo = awardService.selectAwardById(id);
        if (awardVo == null){
            throw new ServiceException("对应奖状不存在!");
        }
        return R.ok(awardService.deleteAwardById(id));
    }




    /**
     * 编辑用户信息
     * @return
     */
    @ApiOperation("编辑用户信息")
    @PostMapping("/updateUserInfo")
    public R updateUserInfo(@RequestBody UserInfo userInfo){
        Long userId = SecurityUtils.getFrontLoginUserId();
        userInfo.setId(userId);
        String password = userInfo.getPassword();
        String encryptPassword = null;
        if (!StringUtils.isEmpty(password)){
            encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        }
        userInfo.setPassword(encryptPassword);
        return R.ok(userInfoService.updateUserInfo(userInfo));
    }

    /**
     * 查询专业信息列表
     */
    @ApiOperation("查询专业信息列表")
    @GetMapping("/getCollegeMajor")
    public R<List<CollegeMajor>> getCollegeMajor(
            @ApiParam(name = "collegeMajor", value = "筛选条件", required = false) CollegeMajor collegeMajor) {
        List<CollegeMajor> list = collegeMajorService.selectCollegeMajorList(collegeMajor);
        return R.ok(list);
    }

    /**
     * 找回密码
     */
    @ApiOperation("找回密码")
    @PostMapping("/findPassword")
    @Transactional
    public R findPassword(@ApiParam(name = "findUserVo", value = "找回密码信息", required = true) @RequestBody FindUserVo findUserVo) {
        //判空
        if (findUserVo == null) {
            throw new ServiceException("参数不能为空!");
        }
        //判断用户是否存在
        String eamil = findUserVo.getEmail();
        UserInfo user = userInfoService.selectUserInfoByEmail(eamil);
        if (user==null){
            throw new ServiceException("用户不存在,请先注册!");
        }
        //判断验证码输入是否正确
        //从缓存中取到code
        String rcode = (String) redisTemplate.opsForValue().get(RedisConstant.EMAIL + eamil);
        if (!rcode.equals(findUserVo.getCode())) {
            throw new ServiceException("验证码错误~");
        }
        //判断两次输入的密码是否一致
        String newPassword = findUserVo.getNewPassword();
        String checkPassword = findUserVo.getCheckPassword();
        if(!newPassword.equals(checkPassword)){
            throw new ServiceException("两次输入的密码不一致,请校验~");
        }
        //密码加密
        String encryptPassword =  DigestUtils.md5DigestAsHex((SALT + newPassword).getBytes());
        user.setPassword(encryptPassword);
        //更新
        return R.ok(userInfoService.updateUserInfo(user));

    }

    /**
     * 根据用户id返回用户所获得的奖项
     */
    @ApiOperation("根据用户id返回该用户所获得的奖项")
    @GetMapping("/getUserAward")
    public R<List<AwardVo>> getawardbyId(
            @ApiParam(value = "用户id", required = true)
            @RequestParam("userId") Long userId){
        List<AwardVo> awards = awardService.selectAwardByUserId(userId);
        return R.ok(awards);
    }



    /**
     * 查询老师或学生信息
     */
    @ApiOperation("查询老师或学生信息")
    @GetMapping("/getAllUser")
    public R<List<UserInfoVo>> getAllUser(@ApiParam(name = "isTeacher", value = "是否为老师标识", required = true)
                                              @RequestParam("isTeacher") Integer isTeacher){
        try {
            List<UserInfoVo> userInfoVoList = userInfoService.selectAllTeachers(isTeacher);
            if (userInfoVoList.isEmpty()) {
                return R.fail("暂无老师信息!");
            }
            return R.ok(userInfoVoList);
        }catch (Exception e){
            return  R.fail("查询失败，请稍后重试！");
        }

    }

    /**
     * 获取登录用户
     */
    @ApiOperation("获取登录用户")
    @GetMapping("/getLoginUser")
    public R<UserInfoVo> getCurrentLoginUser(){
        UserInfo loginUser = userInfoService.getLoginUser();
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtil.copyProperties(loginUser,userInfoVo);
        return R.ok(userInfoVo);
    }


    /**
     * 用户上传奖状功能,String类型
     */
    @ApiOperation("用户上传奖状功能")
    @PostMapping("/uploadAward")
    @Transactional
    public R<AwardRecords> uploadAward(@ApiParam(name = "file", value = "图片路径", required = true)
            @RequestParam("file") MultipartFile file,
            @ApiParam(name = "name", value = "奖状参与的项目名称", required = true)
            @RequestParam("name")String name) {
        try {
            // 校验文件是否为空
            if (file.isEmpty()) {
                throw new ServiceException("上传奖状不能为空");
            }

            //项目名称不能为空
            if(StringUtils.isEmpty(name)){
                throw new ServiceException("请输入获奖所参与的项目~");
            }

            //奖状后缀校验
            String originalFilename = file.getOriginalFilename();
            String suffix = FileUtil.getSuffix(originalFilename);
            final List<String> validSuffxisList = Arrays.asList("jpg", "jpeg", "png","JPG","JPEG","PNG");
            if (!validSuffxisList.contains(suffix)){
                throw new ServiceException("请上传正确的图片格式");
            }

            // 使用若依的工具类上传文件
            //String filePath = FileUploadUtils.upload(file);
            // 使用阿里云的工具类上传文件
            String filePath = aliOssUtil.upload(file.getBytes(), file.getOriginalFilename());

            // 返回文件访问地址
            AwardRecords awardRecords = new AwardRecords();
            awardRecords.setAwardUrl(filePath);
            String[] split = name.split(",");
            awardRecords.setCompetitionName(split[0]);
            awardRecords.setUser(userInfoService.getLoginUser().getId());
            int i = awardRecordsService.insertAwardRecords(awardRecords);
            if (i>0){
                return R.ok(awardRecords);
            }else {
                throw new ServiceException("上传文件失败");
            }
        } catch (Exception e) {
            throw new ServiceException("上传失败:"+e.getMessage());
        }
    }
    /**
     * 上传/更新用户头像
     */
    @ApiOperation("上传/更新用户头像")
    @PostMapping("/uploadAvatar")
    @Transactional
    public R<String> uploadAvatar(
            @ApiParam(name = "file",value = "头像图片文件",required = true)
            @RequestParam("file") MultipartFile file) throws IOException {
        try {
            // 校验文件是否为空
            if (file.isEmpty()) {
                throw new ServiceException("上传头像不能为空");
            }

            //校验头像文件格式
            String originalFilename = file.getOriginalFilename();
            String suffix = FileUtil.getSuffix(originalFilename);
            final List<String> validSuffixList = Arrays.asList("jpg", "jpeg", "png","JPG","JPEG","PNG");
            if (!validSuffixList.contains(suffix)){
                throw new ServiceException("请上传正确的图片格式");
            }

            //校验文件大小(最大不超过2MB）
            long maxFileSize = 2*1024*1024;
            if (file.getSize()>maxFileSize){
                throw new ServiceException("头像文件不能超过2MB，请重新上传");
            }

            //获取用户ID
            Long loginUserId=SecurityUtils.getFrontLoginUserId();

            //查询用户原有头像url
            UserInfo loginUser = userInfoService.getLoginUser();
            String AvatarUrl=loginUser.getAvatarUrl();

            if (AvatarUrl!=null){
                //如果不为空，从旧头像url中解析出来oss的文件路径
                String ossFilePath=AvatarUrl.substring(AvatarUrl.indexOf("user-avatars/"));
                boolean deleteSuccess=aliOssUtil.delete(ossFilePath);

            }


            //生成唯一文件名
            String uniqueFileName="user-avatars/"+loginUserId+"-"+System.currentTimeMillis()+"."+suffix;

            // 使用阿里云的工具类上传文件
            String avatarOssUrl =aliOssUtil.upload(file.getBytes(), uniqueFileName);
            if (StringUtils.isEmpty(avatarOssUrl)){
                throw new ServerException("头像上传到oss失败，请重试");
            }

            //更新数据库中的用户头像URL
            // 创建UserInfo对象，设置需更新的字段
            UserInfo userInfoToUpdate = new UserInfo();
            userInfoToUpdate.setId(loginUserId);//主键
            userInfoToUpdate.setAvatarUrl(avatarOssUrl);//头像URL
            userInfoToUpdate.setUpdateTime(DateUtils.getNowDate());//更新时间

            //调用service，完成更新
            int updateResult = userInfoService.updateUserInfo(userInfoToUpdate);
            if (updateResult<=0){
                throw new ServerException("头像URL更新失败");
            }
            //返回头像的URL地址
            return R.ok(avatarOssUrl);
        }
        catch (Exception e){
            throw new ServiceException("上传失败:"+e.getMessage());
        }

    }

    /**
     * 查询用户上传的奖状
     */
    @ApiOperation("查询用户上传的奖状")
    @GetMapping("/getAward")
    public R<List<AwardRecords>> getAward() {
        return R.ok(awardRecordsService.getAwardByUserId(userInfoService.getLoginUser().getId()));
    }





    @ApiOperation("查询通知列表")
    @GetMapping("/getNotice")
    public R<List<Notice>> getNoticeList() {
        Long userId = SecurityUtils.getFrontLoginUserId();
        Notice notice = new Notice();
        notice.setUserId(userId);
        notice.setIsUnread(0);
        return R.ok(noticeService.selectNoticeList(notice));
    }

    @ApiOperation("已读通知")
        @PutMapping("/readNotice/{id}")
    public R readNotice(@PathVariable("id") Long id) {
        Notice notice = noticeService.selectNoticeById(id);
        notice.setIsUnread(1);
        noticeService.updateNotice(notice);
        return R.ok();
    }




}
