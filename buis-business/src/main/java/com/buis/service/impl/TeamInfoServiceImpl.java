package com.buis.service.impl;

import java.util.List;
import com.buis.common.utils.DateUtils;
import com.buis.domain.TeamMemberTeacher;
import com.buis.dto.TeamQueryDTO;
import com.buis.mapper.TeamMemberTeacherMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.buis.mapper.TeamInfoMapper;
import com.buis.domain.TeamInfo;
import com.buis.service.ITeamInfoService;

/**
 * 队伍信息Service业务层处理
 * 
 * @author buis
 * @date 2024-10-11
 */
@Service
public class TeamInfoServiceImpl implements ITeamInfoService 
{
    @Autowired
    private TeamInfoMapper teamInfoMapper;


    /**
     * 查询队伍信息
     * 
     * @param id 队伍信息主键
     * @return 队伍信息
     */
    @Override
    public TeamInfo selectTeamInfoById(Long id)
    {
        return teamInfoMapper.selectTeamInfoById(id);
    }

    /**
     * 查询队伍信息列表
     * 
     * @param teamInfo 队伍信息
     * @return 队伍信息
     */
    @Override
    public List<TeamInfo> selectTeamInfoList(TeamInfo teamInfo)
    {
        return teamInfoMapper.selectTeamInfoList(teamInfo);
    }

    /**
     * 新增队伍信息
     * 
     * @param teamInfo 队伍信息
     * @return 结果
     */
    @Override
    public int insertTeamInfo(TeamInfo teamInfo)
    {
        teamInfo.setCreateTime(DateUtils.getNowDate());
        return teamInfoMapper.insertTeamInfo(teamInfo);
    }

    /**
     * 修改队伍信息
     * 
     * @param teamInfo 队伍信息
     * @return 结果
     */
    @Override
    public int updateTeamInfo(TeamInfo teamInfo)
    {
        teamInfo.setUpdateTime(DateUtils.getNowDate());
        return teamInfoMapper.updateTeamInfo(teamInfo);
    }

    /**
     * 批量删除队伍信息
     * 
     * @param ids 需要删除的队伍信息主键
     * @return 结果
     */
    @Override
    public int deleteTeamInfoByIds(Long[] ids)
    {
        return teamInfoMapper.deleteTeamInfoByIds(ids);
    }

    /**
     * 删除队伍信息信息
     * 
     * @param id 队伍信息主键
     * @return 结果
     */
    @Override
    public int deleteTeamInfoById(Long id)
    {
        return teamInfoMapper.deleteTeamInfoById(id);
    }

    @Override
    public List<TeamInfo> listTeams(TeamQueryDTO teamQueryDTO) {
        //TODO 需要在查出来的时候判断我是否加入该队伍吗?
        return teamInfoMapper.listTeams(teamQueryDTO);
    }

}
