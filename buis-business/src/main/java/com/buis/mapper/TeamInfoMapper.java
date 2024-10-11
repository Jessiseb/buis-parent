package com.buis.mapper;

import java.util.List;
import com.buis.domain.TeamInfo;

/**
 * 队伍信息Mapper接口
 * 
 * @author buis
 * @date 2024-10-11
 */
public interface TeamInfoMapper 
{
    /**
     * 查询队伍信息
     * 
     * @param id 队伍信息主键
     * @return 队伍信息
     */
    public TeamInfo selectTeamInfoById(Long id);

    /**
     * 查询队伍信息列表
     * 
     * @param teamInfo 队伍信息
     * @return 队伍信息集合
     */
    public List<TeamInfo> selectTeamInfoList(TeamInfo teamInfo);

    /**
     * 新增队伍信息
     * 
     * @param teamInfo 队伍信息
     * @return 结果
     */
    public int insertTeamInfo(TeamInfo teamInfo);

    /**
     * 修改队伍信息
     * 
     * @param teamInfo 队伍信息
     * @return 结果
     */
    public int updateTeamInfo(TeamInfo teamInfo);

    /**
     * 删除队伍信息
     * 
     * @param id 队伍信息主键
     * @return 结果
     */
    public int deleteTeamInfoById(Long id);

    /**
     * 批量删除队伍信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTeamInfoByIds(Long[] ids);
}
