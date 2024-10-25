package com.buis.service.impl;

import com.buis.common.core.domain.AjaxResult;
import com.buis.common.utils.DateUtils;
import com.buis.domain.TeamMemberTeacher;
import com.buis.mapper.TeamMemberTeacherMapper;
import com.buis.service.ITeamMemberTeacherService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;





/**
 * 队伍老师管理Service业务层处理
 * 
 * @author buis
 * @date 2024-10-11
 */
@Slf4j
@Service
public class TeamMemberTeacherServiceImpl implements ITeamMemberTeacherService
{
    @Autowired
    private TeamMemberTeacherMapper teamMemberTeacherMapper; //mapper代理对象
    //日志
    private static final Logger logger = LoggerFactory.getLogger(TeamMemberTeacherServiceImpl.class);
    /**
     * 查询队伍老师管理
     * 
     * @param id 队伍老师管理主键
     * @return 队伍老师管理
     */
    @Transactional(readOnly = true)
    @Override
    public TeamMemberTeacher selectTeamMemberTeacherById(Long id)
    {
        log.debug("查询队伍老师管理信息，ID：{}", id); // 日志记录查询操作
        // 首先检查id是否为空
        if (id == null) {
            // id为空，抛出异常
             throw new IllegalArgumentException("ID cannot be null");
        }

        // 调用Mapper方法查询队伍老师管理信息
        TeamMemberTeacher teamMemberTeacher = teamMemberTeacherMapper.selectTeamMemberTeacherById(id);

        // 检查查询结果是否为空
        if (teamMemberTeacher == null) {
            // 查询不到数据时，抛出异常
            log.error("未找到ID为{}的队伍老师管理信息", id);
            throw new EntityNotFoundException("TeamMemberTeacher");
        }

        // 返回查询到的队伍老师管理信息
        return teamMemberTeacher;
    }




    /**
     * 查询队伍老师管理列表
     * 
     * @param teamMemberTeacher 队伍老师管理
     * @return 队伍老师管理
     */
    @Transactional(readOnly = true)
    @Override
    public List<TeamMemberTeacher> selectTeamMemberTeacherList(TeamMemberTeacher teamMemberTeacher)
    {
        log.debug("新增队伍老师管理，信息：{}", teamMemberTeacher); // 日志记录新增操作
        return teamMemberTeacherMapper.selectTeamMemberTeacherList(teamMemberTeacher);
    }

    /**
     * 新增队伍老师管理
     * 
     * @param teamMemberTeacher 队伍老师管理
     * @return 结果
     */
    @Override
    public int insertTeamMemberTeacher(TeamMemberTeacher teamMemberTeacher)
    {
        log.debug("查询队伍老师管理列表，过滤条件：{}", teamMemberTeacher); // 日志记录查询操作
        teamMemberTeacher.setCreateTime(DateUtils.getNowDate());
        return teamMemberTeacherMapper.insertTeamMemberTeacher(teamMemberTeacher);
    }

    /**
     * 修改队伍老师管理
     *
     * @param teamMemberTeacher 队伍老师管理
     * @return 结果
     */
    @Override
    public int updateTeamMemberTeacher(TeamMemberTeacher teamMemberTeacher)
    {
        log.debug("修改队伍老师管理，信息：{}", teamMemberTeacher); // 日志记录更新操作
        // 数据验证
        if (teamMemberTeacher.getId() == null) {
            log.error("更新队伍老师管理失败，ID为空"); // 日志记录错误信息
            throw new IllegalArgumentException("ID cannot be null");
        }

        // 执行更新操作
        int affectedRows = teamMemberTeacherMapper.updateTeamMemberTeacher(teamMemberTeacher);

        // 检查更新是否成功
        if (affectedRows <= 0) {
            throw  new IllegalArgumentException("update failed,no rows affected");
        }
        teamMemberTeacher.setUpdateTime(DateUtils.getNowDate()); //最后修改时间

        return affectedRows;
    }



    /**
     * 批量删除队伍老师管理
     *
     * @param ids 需要删除的队伍老师管理主键
     * @return 结果
     */
    @Override
    public int deleteTeamMemberTeacherByIds(Long[] ids)
    {
        log.debug("批量删除队伍老师管理，IDs：{}", ids); // 日志记录删除操作
        if (ids == null || ids.length == 0) {
            log.error("批量删除队伍老师管理失败，ID为空"); // 日志记录错误信息
            throw new IllegalArgumentException("IDs cannot be null or empty");
        }

        int count = 0;
        for (Long id : ids) {
            // 可以在这里添加更多的业务逻辑，比如检查记录是否存在
            TeamMemberTeacher teamMemberTeacher = teamMemberTeacherMapper.selectTeamMemberTeacherById(id);
            if (teamMemberTeacher == null) {
               throw new EntityNotFoundException("TeamMemberTeacher"+id);
            }

            // 执行删除操作
            int result = teamMemberTeacherMapper.deleteTeamMemberTeacherById(id);
            if (result > 0) {
                count++;
            }
        }
        // 可以根据count的值来判断是否所有记录都删除成功
        if (count != ids.length) {
            throw new RuntimeException("Failed to delete all team member teachers");
        }

        return count;

    }

    /**
     * 删除队伍老师管理信息
     * 
     * @param id 队伍老师管理主键
     * @return 结果
     */
    @Override
    public AjaxResult deleteTeamMemberTeacherById(Long id)
    {
        log.debug("删除队伍老师管理，ID：{}", id); // 日志记录删除操作
        // 数据验证
        if (id == null) {
            log.error("删除队伍老师管理失败，ID为空"); // 志记录错误信息
            throw new IllegalArgumentException("ID cannot be null");
        }
        TeamMemberTeacher teamMemberTeacher = teamMemberTeacherMapper.selectTeamMemberTeacherById(id);
        Long record_id = teamMemberTeacher.getId();
        if (record_id == null){
            return AjaxResult.error(404,"数据不存在");
        }

        // 调用Mapper方法删除队伍老师管理信息
        return AjaxResult.success(teamMemberTeacherMapper.deleteTeamMemberTeacherById(id));

    }
}
