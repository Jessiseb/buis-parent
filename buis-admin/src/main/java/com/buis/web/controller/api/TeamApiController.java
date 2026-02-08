package com.buis.web.controller.api;


import cn.dev33.satoken.stp.StpUtil;
import com.buis.common.constant.HttpStatus;
import com.buis.model.bo.MemberInfoBO;
import com.buis.model.bo.TeacherInfoBO;
import com.buis.common.config.RuoYiConfig;
import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.AjaxResult;
import com.buis.common.core.domain.R;
import com.buis.common.core.page.MyPageVo;
import com.buis.common.exception.ServiceException;
import com.buis.common.utils.SecurityUtils;
import com.buis.common.utils.StringUtils;
import com.buis.common.utils.file.FileUploadUtils;
import com.buis.common.utils.file.FileUtils;
import com.buis.model.domain.*;
import com.buis.model.dto.TeamMemberDto;
import com.buis.model.enums.ChooseStatus;
import com.buis.model.enums.NoticeType;
import com.buis.framework.config.ServerConfig;
import com.buis.mapper.TeamInfoMapper;
import com.buis.service.*;
import com.buis.model.vo.TeamInfoVo;
import com.buis.model.vo.TeamMemberStudentVo;
import com.buis.model.vo.TeamMemberTeacherVo;
import com.buis.model.vo.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 项目团队管理
 */
@Api(tags = "项目团队管理Api")
@RestController
@RequestMapping("/api/team")
@Slf4j
public class TeamApiController extends BaseController {

    @Autowired
    private ITeamMemberStudentService teamMemberStudentService;
    @Autowired
    private ITeamMemberTeacherService teamMemberTeacherService;
    @Autowired
    private ITeamInfoService teamInfoService;
    @Autowired
    private IRaceInfoService raceInfoService;
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private ServerConfig serverConfig;
    @Autowired
    private IEmailService emailService;
    @Autowired
    private IPreSignStudentService preSignStudentService;
    @Autowired
    private INoticeService noticeService;
    @Autowired
    private IExchangeCodeService codeService;
    @Resource
    private TeamInfoMapper teamInfoMapper;


    private static final String FILE_DELIMETER = ",";

    /**
     * 抽取出判断登录用户是否为指导老师或者团队负责人
     */
    public boolean isTeamLeaderOrTeacher(Long team) {
        //获取登录用户
        String loginIdStr = StpUtil.getLoginId().toString();
        Long loginId;
        try {
            loginId = Long.parseLong(loginIdStr);
        } catch (NumberFormatException e) {
            throw new ServiceException("无效的用户ID格式");
        }
        //获取用户信息
        UserInfo loginUser = userInfoService.getLoginInfoByUserId(loginId);
        //判断用户是否为老师
        if (loginUser.getIsTeacher() == 1) {
            //是老师
            //判断是否为本项目团队的指导老师
            //根据team查出队伍
            TeamInfoVo teamInfoVo = teamInfoService.selectTeamInfoById(team);
            //是指导老师
            return teamInfoVo.getTeacher1().equals(loginUser.getName()) || teamInfoVo.getTeacher2().equals(loginUser.getName());
        }
        //判断用户是否是负责人
        TeamInfoVo teamInfoVo = teamInfoService.selectTeamInfoById(team);
        Long leader = teamInfoVo.getLeader();
        if (leader.equals(loginId)) {
            //是负责人
            return true;
        }
        return false;
    }


    /**
     * 添加成员
     *
     * @param userInfoVoList 用户信息列表
     * @param team           团队id
     * @return
     */
    @ApiOperation("添加成员(邀请)")
    @PostMapping("/addTeamMember")
    @Transactional
    public R<Long> addTeamMember(
            @ApiParam(name = "userInfoVoList", value = "用户信息列表")
            @RequestBody List<UserInfoVo> userInfoVoList,
            @ApiParam(name = "team", value = "团队id")
            @RequestParam(value = "teamId") Long team) {
        //添加的时候要判断团队是否存在
        //根据team查询队伍信息
        TeamInfo teamInfo = teamInfoService.selectTeamInfoById(team);
        if (teamInfo == null) {
            throw new ServiceException("项目团队不存在,请先创建团队~");
        }
        //如果已经邀请过对方就提示已邀请,请勿重复邀请
        for (UserInfoVo userInfoVo : userInfoVoList) {
            if (teamMemberStudentService.getRequestCount(userInfoVo.getId(),teamInfo.getRace(),team)>=1) {
                log.info("您已经邀请过学号为:"+userInfoVo.getNumber()+"的用户，请勿重复邀请");
                throw new ServiceException("您已经邀请过学号为:"+userInfoVo.getNumber()+"的用户，请勿重复邀请");
            }
        }

        // 检查邀请的成员是否已经预报名比赛
//        for (UserInfoVo userInfoVo : userInfoVoList) {
//
//            PreSignStudent preSignStudent = preSignStudentService.selectByUserIdAndRace(userInfoVo.getId(), teamInfo.getRace());
//            if (preSignStudent == null || preSignStudent.getIsDeleted() == 1) {
//                throw new ServiceException("学号为:" + userInfoVo.getNumber() + "的用户尚未预报名该比赛");
//            }
//        }

        //只有指导老师和负责人才有权限
        if (!isTeamLeaderOrTeacher(team)) {
            throw new ServiceException("您不是团队负责人或指导老师，没有权限进行此操作");
        }
        //批量插入成员
        List<TeamMemberStudent> teamMemberStudentList = new ArrayList<>();
        for (UserInfoVo userInfoVo : userInfoVoList) {
            TeamMemberStudent teamMemberStudent = new TeamMemberStudent();
            teamMemberStudent.setTeam(teamInfo.getId()); //队伍id
            teamMemberStudent.setRace(teamInfo.getRace()); //比赛id
            teamMemberStudent.setStudent(userInfoVo.getId()); //成员
            teamMemberStudent.setDecider(teamInfo.getLeader()); //负责人
            teamMemberStudent.setIsChoosed(ChooseStatus.TOBESELECTED.getCode()); //选定状态 0待同意
            teamMemberStudentList.add(teamMemberStudent);
        }
        //插入
        int ok = teamMemberStudentService.insertTeamMemberStudentByList(teamMemberStudentList);
        //邀请之后发送邮箱通知 + 个人中心通知
        for (TeamMemberStudent teamMemberStudent : teamMemberStudentList) {
            emailService.inviteMember(teamMemberStudent.getTeam(), teamMemberStudent.getStudent());

            Notice notice = new Notice();
            notice.setUserId(teamMemberStudent.getStudent());
            notice.setTeamId(teamMemberStudent.getTeam());
            notice.setType(NoticeType.Team.getCode());
            notice.setTitle("组队邀请");
            notice.setContent(teamInfo.getLeaderName() + "邀请你加入" + "'" + teamInfo.getName() + "'" + "项目团队");
            noticeService.insertNotice(notice);
        }
        if (ok == 0) {
            throw new ServiceException("添加成员失败");
        }
        //返回团队id
        return R.ok(team);
    }

    /**
     * 修改用户与队伍互选关系
     * @param isTeacher
     * @param teamMemberDto
     * @return
     */
    @ApiOperation("修改用户与队伍互选关系")
    @PutMapping("/updateTeamMember/{isTeacher}/{id}")
    public R updateTeamMember(
            @ApiParam(name = "isTeacher", value = "用户类型，0学生，1老师", required = true) @PathVariable(value = "isTeacher") Long isTeacher,
            @ApiParam(name = "id", value = "记录ID", required = true) @PathVariable(value = "id") Long id,
            @RequestBody TeamMemberDto teamMemberDto) {
        //只有老师和负责人才能修改


        if (isTeacher != null) {
            if (isTeacher == 0) { // 用户是学生
                //把teamMemberDtoList分别buildTeamMemberStudent和setId
                TeamMemberStudent teamMemberStudent = buildTeamMemberStudent(teamMemberDto);
                teamMemberStudent.setId(id);
                teamMemberStudentService.updateTeamMemberStudent(teamMemberStudent);
            } else if (isTeacher == 1) { // 用户是老师
                TeamMemberTeacher teamMemberTeacher = buildTeamMemberTeacher(teamMemberDto);
                teamMemberTeacher.setId(id);
                teamMemberTeacherService.updateTeamMemberTeacher(teamMemberTeacher);
            }
        }
        return R.ok();
    }

    /**
     * 构建 TeamMemberStudent 对象
     */
    private TeamMemberStudent buildTeamMemberStudent(TeamMemberDto teamMemberDto) {
        TeamMemberStudent teamMemberStudent = new TeamMemberStudent();
        teamMemberStudent.setStudent(teamMemberDto.getUser());
        teamMemberStudent.setTeam(teamMemberDto.getTeam());
        teamMemberStudent.setDecider(teamMemberDto.getDecisionRole());
        teamMemberStudent.setReason(teamMemberDto.getReason());
        teamMemberStudent.setRace(teamMemberDto.getRace());
        teamMemberStudent.setIsChoosed(teamMemberDto.getIsChoosed());
        return teamMemberStudent;
    }

    /**
     * 构建 TeamMemberTeacher 对象
     */
    private TeamMemberTeacher buildTeamMemberTeacher(TeamMemberDto teamMemberDto) {
        TeamMemberTeacher teamMemberTeacher = new TeamMemberTeacher();
        teamMemberTeacher.setTeacher(teamMemberDto.getUser());
        teamMemberTeacher.setTeam(teamMemberDto.getTeam());
        teamMemberTeacher.setDecider(teamMemberDto.getDecisionRole());
        teamMemberTeacher.setReason(teamMemberDto.getReason());
        teamMemberTeacher.setRace(teamMemberDto.getRace());
        teamMemberTeacher.setIsChoosed(teamMemberDto.getIsChoosed());
        return teamMemberTeacher;
    }

    /**
     * 踢人接口
     * 只有负责人和老师才有权限
     * 要判断是否为指导老师
     *
     * @param team
     * @param user
     * @param isTeacher
     * @return
     */
    @ApiOperation("剔除团队成员")
    @DeleteMapping("/deleteTeamMember/{team}/{user}/{isTeacher}")
    public R delectTeamMember(
            @ApiParam(name = "team", value = "队伍id", required = true) @PathVariable(value = "team") Long team,
            @ApiParam(name = "user", value = "用户id", required = true) @PathVariable(value = "user") Long user,
            @ApiParam(name = "isTeacher", value = "用户类型，0学生，1老师", required = true) @PathVariable(value = "isTeacher") Long isTeacher) {

        if (isTeacher != null && isTeacher == 0) { // 用户是学生
            teamMemberStudentService.deleteTeamMemberStudentByTeamAndStudent(team, user);
        } else if (isTeacher != null && isTeacher == 1) { // 用户是老师
            teamMemberTeacherService.deleteTeamMemberTeacherByTeamAndTeacher(team, user);
        }
        return R.ok();
    }

    /**
     * 带条件分页查询学生与队伍互选邀请
     *
     * @param teamMemberStudent
     * @return
     */
    @ApiOperation("带条件分页查询学生与队伍互选邀请")
    @PostMapping("/getTeamMemberStudent")
    public MyPageVo<List<TeamMemberStudentVo>> getTeamMemberStudent(@RequestBody(required = false) TeamMemberStudent teamMemberStudent,
                                                                    @ApiParam(name = "pageNum", value = "查询页数", required = true) @RequestParam Integer pageNum,
                                                                    @ApiParam(name = "pageSize", value = "查询数量", required = true) @RequestParam Integer pageSize) {
        startPage();
        List<TeamMemberStudentVo> list = teamMemberStudentService.selectTeamMemberStudentList(teamMemberStudent);
        return new MyPageVo<>(list);
    }

    /**
     * 带条件分页查询老师与队伍互选邀请
     *
     * @param teamMemberTeacher
     * @return
     */
    @ApiOperation("带条件分页查询老师与队伍互选邀请")
    @PostMapping("/getTeamMemberTeacher")
    public MyPageVo<List<TeamMemberTeacherVo>> getTeamMemberTeacher(@RequestBody(required = false) TeamMemberTeacher teamMemberTeacher,
                                                                    @ApiParam(name = "pageNum", value = "查询页数", required = true) @RequestParam Integer pageNum,
                                                                    @ApiParam(name = "pageSize", value = "查询数量", required = true) @RequestParam Integer pageSize) {
        startPage();
        List<TeamMemberTeacherVo> list = teamMemberTeacherService.selectTeamMemberTeacherList(teamMemberTeacher);
        return new MyPageVo<>(list);
    }

    /**
     * 根据队伍id查询队伍成员列表(作废)
     * @param pageNum
     * @param pageSize
     * @param team
     * @return
     */
    @ApiOperation("根据队伍id查询队伍成员列表")
    @GetMapping("/Member/{team}")
    public R<HashMap<String, List<UserInfoVo>>> getMemberStudentByTeamId(
            @ApiParam(name = "pageNum", value = "查询页数", required = true) @RequestParam Integer pageNum,
            @ApiParam(name = "pageSize", value = "查询数量", required = true) @RequestParam Integer pageSize,
            @ApiParam(name = "team", value = "队伍id", required = true) @PathVariable("team") Long team) {
        List<UserInfoVo> student = teamMemberStudentService.getMemberStudentByTeamId(team);
        List<UserInfoVo> teacher = teamMemberTeacherService.getMemberTeachersByTeamId(team);
        HashMap<String, List<UserInfoVo>> map = new HashMap<>();
        map.put("student", student);
        map.put("teacher", teacher);
        return R.ok(map);
    }


    /**
     * 查询队伍信息列表
     * @param pageNum
     * @param pageSize
     * @param teamInfo
     * @param userId 该登录用户的id 要不要都可以
     * @return
     */
    @ApiOperation("带条件分页查询队伍列表")
    @PostMapping("/list")
    public MyPageVo list(
            @ApiParam(name = "pageNum", value = "查询页数", required = true) @RequestParam Integer pageNum,
            @ApiParam(name = "pageSize", value = "查询数量", required = true) @RequestParam Integer pageSize,
            @RequestBody(required = false) TeamInfo teamInfo,
            @ApiParam(name = "userId", value = "用户id", required = false)
            @RequestParam(required = false) Long userId,
            @ApiParam(name = "isPublish", value = "是否发布招募",required = false)
            @RequestParam(required = false) String isPublish){
        startPage();

        //如果userId不为空,则查询这个用户所参与的队伍
        if (userId != null) {
            List<TeamMemberStudent> teamMemberStudents = teamMemberStudentService.selectTeamMemberStudentByUserId(userId);
            List<Long> collect = teamMemberStudents.stream()
                    .map(TeamMemberStudent::getTeam)
                    .collect(Collectors.toList());
            //转成数组类型
            if (collect.size() != 0) {
                Long[] ids = collect.toArray(new Long[collect.size()]);
                return new MyPageVo(teamInfoService.selectTeamInfoByIds(ids));
            } else {
                return new MyPageVo();
            }

        }
        teamInfo.setIsPublish(isPublish);//查出已发布招募的队伍信息
        List<TeamInfoVo> list = null;
        //如果teamInfo有值,则查询带条件的队伍信息
        if (teamInfoService.hasSearchCondition(teamInfo)) {
            list = teamInfoService.selectTeamInfoList(teamInfo);
            return new MyPageVo(list);
        } else {
            return teamInfoService.selectTeamInfoListFromRedis(teamInfo);
        }
    }

    /**
     * 根据项目队伍id查询团队信息
     */
    @ApiOperation("根据项目团队id查询团队信息")
    @GetMapping("/{id}")
    public R<TeamInfoVo> getTeamInfoById(@ApiParam(name = "id", value = "项目队伍id", required = true)
                                         @PathVariable("id") Long id) {
        TeamInfoVo teamInfoVo = teamInfoService.selectTeamInfoById(id);
        if (teamInfoVo == null) {
            return R.fail("查询失败");
        }

        //根据团队id获取到团队成员信息
        List<TeamMemberStudent> teamMemberStudents = teamMemberStudentService.selectTeamMemberStudentByTeamId(id);
        //获取到对应的用户信息
        List<UserInfoVo> students = userInfoService.selectUserInfoVoByIds(teamMemberStudents.stream()
                .map(TeamMemberStudent::getStudent)
                .collect(Collectors.toList()));
        //获取到指导老师信息
        List<TeamMemberTeacher> teamMemberTeachers = teamMemberTeacherService.selectTeamMemberTeacherByTeamId(id);
        //获取到对应的用户信息
        List<UserInfoVo> teachers = userInfoService.selectUserInfoVoByIds(teamMemberTeachers.stream()
                .map(TeamMemberTeacher::getTeacher)
                .collect(Collectors.toList()));
        //students转换成BO
        List<MemberInfoBO> teamMemberStudentsBO = students.stream()
                .map(userInfoVo -> {
                    MemberInfoBO memberStudentBO = new MemberInfoBO();
                    BeanUtils.copyProperties(userInfoVo, memberStudentBO);
                    return memberStudentBO;
                })
                .collect(Collectors.toList());
        //teacher转换成BO
        List<TeacherInfoBO> teamMemberTeachersBO = teachers.stream()
                .map(userInfoVo -> {
                    TeacherInfoBO memberTeacherBO = new TeacherInfoBO();
                    BeanUtils.copyProperties(userInfoVo, memberTeacherBO);
                    return memberTeacherBO;
                })
                .collect(Collectors.toList());
        //把团队成员信息返回给teamInfoVo
        teamInfoVo.setMemberInfoBOList(teamMemberStudentsBO);
        teamInfoVo.setTeacherInfoBOList(teamMemberTeachersBO);
        return R.ok(teamInfoVo);
    }


    /**
     * 根据老师id查询老师指导的队伍列表
     *
     * @return 返回结果
     */
    @ApiOperation("带条件分页查询老师指导的队伍列表")
    @PostMapping("/guide")
    public MyPageVo<List<TeamInfoVo>> guide(@ApiParam(name = "pageNum", value = "查询页数", required = true) @RequestParam Integer pageNum,
                                            @ApiParam(name = "pageSize", value = "查询数量", required = true) @RequestParam Integer pageSize,
                                            @ApiParam(name = "race", value = "比赛id", required = false) @RequestParam(required = false) Long race,
                                            @ApiParam(name = "name", value = "比赛名字", required = false) @RequestParam(required = false) Long name
                                            ) {
        startPage();
        Long userId = userInfoService.getLoginUser().getId();
        List<TeamInfoVo> teamInfoVos = teamMemberTeacherService.selectJoinTeamByTeacherId(userId,race,name);
        return new MyPageVo(teamInfoVos);
    }


    /**
     * 新增队伍信息
     * @param teamInfo 队伍信息对象
     * @return R
     */
    @ApiOperation("新增队伍信息(创建项目)")
    @PostMapping("/createTeam")
    public R<TeamInfo> addTeamInfo(@RequestBody TeamInfo teamInfo) {
        //获取当前创建队伍的用户id api/team/api/team
        Long userId = userInfoService.getLoginUser().getId();
        UserInfo createUser = userInfoService.getLoginInfoByUserId(userId);
        teamInfo.setLeader(userId);
        //设置团队的所属学院学院为负责人所在学院
        teamInfo.setCollege(createUser.getCollege());
        TeamInfo result =  teamInfoService.insertTeamInfo(teamInfo);
        //生成唯一且随机的项目编码
        Long teamId =  result.getId();
        if (teamId == null){
            throw new ServiceException("参数错误:teamId为空!");
        }
        String code = codeService.generateCode(teamId);
        result.setProjectCode(code);
        int i = teamInfoMapper.updateTeamInfo(result);
        if (i > 0) {
            teamInfoService.updateTeamInfoCache(result);
        }

        return R.ok(result, "操作成功");
    }


    /**
     * 修改队伍信息
     *
     * @param teamInfo 队伍信息对象
     * @param teamId       队伍ID
     * @return R
     */
    @ApiOperation("修改队伍信息")
    @PutMapping("/update")
    @Transactional
    public R<TeamInfo> editTeamInfo(
            @ApiParam(name = "teamInfo", value = "队伍信息", required = true)
            @RequestBody TeamInfo teamInfo,
            @ApiParam(name = "id", value = "队伍id", required = true)
            @RequestParam("teamId") Long teamId) {
        teamInfo.setId(teamId);
        int result = teamInfoService.updateTeamInfo(teamInfo);
        if (result == 0) {
            return R.fail("操作失败");
        }
        return R.ok(teamInfo, "操作成功");
    }


    /**
     * 删除队伍信息(解散项目团队)
     *
     * @param id 队伍ID
     * @return R
     */
    @ApiOperation("删除队伍信息(解散项目团队)")
    @DeleteMapping("/{id}")
    @Transactional
    public R removeTeamInfo(@ApiParam(name = "id", value = "队伍id", required = true)@PathVariable Long id) {
        int result = teamInfoService.deleteTeamInfoById(id);
        return R.ok(result, "操作成功");
    }

    /**
     * 加入队伍
     *
     */
    @ApiOperation("加入队伍")
    @PostMapping("/joinTeam")
    @Transactional
    public R JoinTeam(@ApiParam(name = "teamMemberDto", value = "加入队伍信息", required = true)
                          @RequestBody TeamMemberDto teamMemberDto) {
       //获取登录用户信息
        UserInfo loginUser = userInfoService.getLoginUser();
        int result = teamInfoService.joinTeamById(teamMemberDto,loginUser);
        if (result <= 0) {
            throw new ServiceException("数据库操作失败", HttpStatus.ERROR);
        }
        //成功后发送邮箱通知
        emailService.requestJoin(teamMemberDto.getTeam());

        return R.ok(result, "操作成功");
    }

    /**
     * 负责人位置转让
     */
    @ApiOperation("负责人位置转让")
    @PostMapping("/transfer/{team}/{user}")
    @Transactional
    public R<Boolean> transfer(@ApiParam(name = "team", value = "队伍id", required = true) @PathVariable("team") Long team,
                      @ApiParam(name = "user", value = "用户id", required = true) @PathVariable("user") Long user) {
        int result = teamInfoService.transfer(team, user);
        if (result == 0) {
            return R.fail("操作失败");
        }
        return R.ok(true, "操作成功");
    }


    /**
     * 退出队伍接口
     */
    @ApiOperation("退出队伍")
    @PostMapping("/quit/{team}")
    public R quit(@ApiParam(name = "team", value = "队伍id", required = true) @PathVariable("team") Long team) {
        //判空
        if (team == null) {
            return R.fail("操作失败");
        }
        //获取登录用户
        Long userId = SecurityUtils.getFrontLoginUserId();
        int result = teamInfoService.quitTeam(team,userId);
        if (result == 0) {
            return R.fail("操作失败");
        }
        return R.ok(result, "操作成功");
    }

    /**
     * 文件上传
     */
    @ApiOperation("前台文件上传")
    @PostMapping("/uploads")
    public AjaxResult uploadFiles(@RequestParam("files") List<MultipartFile> files) throws Exception
    {
        try
        {
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();
            List<String> urls = new ArrayList<String>();
            List<String> fileNames = new ArrayList<String>();
            List<String> newFileNames = new ArrayList<String>();
            List<String> originalFilenames = new ArrayList<String>();
            for (MultipartFile file : files)
            {
                // 上传并返回新文件名称
                String fileName = FileUploadUtils.upload(filePath, file);
                String url = serverConfig.getUrl() + fileName;
                urls.add(url);
                fileNames.add(fileName);
                newFileNames.add(FileUtils.getName(fileName));
                originalFilenames.add(file.getOriginalFilename());
            }
            AjaxResult ajax = AjaxResult.success();
            ajax.put("urls", StringUtils.join(urls, FILE_DELIMETER));
            ajax.put("fileNames", StringUtils.join(fileNames, FILE_DELIMETER));
            ajax.put("newFileNames", StringUtils.join(newFileNames, FILE_DELIMETER));
            ajax.put("originalFilenames", StringUtils.join(originalFilenames, FILE_DELIMETER));
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 同意用户加入队伍
     */
    @ApiOperation("同意加入队伍")
    @PostMapping("/agreeJoinTeam")
    public R<String> agreeJoinTeam(@RequestParam("userId") Long userId,@RequestParam("teamId") Long teamId) {
        int result = teamInfoService.agreeJoinTeam(userId,teamId);
        if (result != 1) {
            return R.fail("操作失败");
        }
        return R.ok("操作成功");
    }

    /**
     * 拒绝用户加入队伍
     */
    @ApiOperation("拒绝用户加入队伍")
    @PostMapping("/refuseJoinTeam")
    public R<String> refuseJoinTeam(@RequestParam("userId") Long userId,@RequestParam("teamId") Long teamId) {
        int result = teamInfoService.refuseJoinTeam(userId,teamId);
        if (result != 1) {
            return R.fail("操作失败");
        }
        return R.ok("操作成功");
    }

    /**
     * 查询自己发布招募的队伍
     */
    @ApiOperation("查询自己发布招募的队伍")
    @PostMapping("/listMyPublishedTeams")
    public MyPageVo listMyPublishedTeams(
            @ApiParam(name = "pageNum", value = "查询页数", required = true) @RequestParam Integer pageNum,
            @ApiParam(name = "pageSize", value = "查询数量", required = true) @RequestParam Integer pageSize,
            @ApiParam(name = "userId", value = "用户id", required = true) @RequestParam Long userId)
    {
        startPage();
        List<TeamInfoVo> list = teamInfoService.selectMyPublishedTeams(userId);
        return new MyPageVo<>(list);
    }
}


