package com.buis.mapper;

import java.util.List;
import com.buis.domain.Award;
import com.buis.vo.AwardVo;
import org.apache.ibatis.annotations.Param;

/**
 * 奖状信息Mapper接口
 * 
 * @author zjx
 * @date 2025-03-16
 */
public interface AwardMapper 
{


    /**
     * 查询奖状信息列表
     * 
     * @param award 奖状信息
     * @return 奖状信息集合
     */
    public List<AwardVo> selectAwardList(AwardVo award);

    /**
     * 新增奖状信息
     * 
     * @param award 奖状信息
     * @return 结果
     */
    public int insertAward(Award award);

    /**
     * 修改奖状信息
     * 
     * @param award 奖状信息
     * @return 结果
     */
    public int updateAward(Award award);

    /**
     * 删除奖状信息
     * 
     * @param id 奖状信息主键
     * @return 结果
     */
    public int deleteAwardById(Long id);

    /**
     * 批量删除奖状信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAwardByIds(Long[] ids);

    /**
     * 根据队伍id查询奖状信息
     * @param name
     * @return
     */

//    Award selectAwardByName(@Param("name") String name);

    Award selectAwardByNameAndRace(@Param("name") String name,@Param("raceId") Long raceId);


    AwardVo selectAwardById(Long id);
}
