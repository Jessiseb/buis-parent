package com.buis.mapper;

import com.buis.model.domain.UserSkill;
import com.buis.model.vo.UserSkillVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户技能关联Mapper接口
 *
 * @author buis
 */
@Repository
public interface UserSkillMapper {

    /**
     * 查询用户技能关联
     *
     * @param id 用户技能关联主键
     * @return 用户技能关联
     */
    UserSkill selectUserSkillById(Long id);

    /**
     * 查询用户技能关联列表
     *
     * @param userSkill 用户技能关联
     * @return 用户技能关联集合
     */
    List<UserSkillVo> selectUserSkillList(UserSkill userSkill);

    /**
     * 根据用户ID查询用户技能列表
     *
     * @param userId 用户ID
     * @return 用户技能列表
     */
    List<UserSkillVo> selectUserSkillsByUserId(Long userId);

    /**
     * 根据技能标签ID查询用户技能关联
     *
     * @param skillTagId 技能标签ID
     * @return 用户技能关联列表
     */
    List<UserSkill> selectUserSkillsBySkillTagId(Long skillTagId);

    /**
     * 根据用户ID列表查询用户技能
     *
     * @param userIds 用户ID列表
     * @return 用户技能列表
     */
    List<UserSkill> selectUserSkillsByUserIds(@Param("userIds") List<Long> userIds);

    /**
     * 新增用户技能关联
     *
     * @param userSkill 用户技能关联
     * @return 结果
     */
    int insertUserSkill(UserSkill userSkill);

    /**
     * 批量新增用户技能关联
     *
     * @param userSkillList 用户技能关联列表
     * @return 结果
     */
    int batchInsertUserSkill(@Param("list") List<UserSkill> userSkillList);

    /**
     * 修改用户技能关联
     *
     * @param userSkill 用户技能关联
     * @return 结果
     */
    int updateUserSkill(UserSkill userSkill);

    /**
     * 删除用户技能关联
     *
     * @param id 用户技能关联主键
     * @return 结果
     */
    int deleteUserSkillById(Long id);

    /**
     * 根据用户ID和技能标签ID删除用户技能关联
     *
     * @param userId 用户ID
     * @param skillTagId 技能标签ID
     * @return 结果
     */
    int deleteUserSkillByUserIdAndSkillTagId(@Param("userId") Long userId, @Param("skillTagId") Long skillTagId);

    /**
     * 批量删除用户技能关联
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteUserSkillByIds(Long[] ids);

    /**
     * 根据用户ID删除该用户的所有技能关联
     *
     * @param userId 用户ID
     * @return 结果
     */
    int deleteUserSkillsByUserId(Long userId);

    /**
     * 统计用户拥有的技能数量
     *
     * @param userId 用户ID
     * @return 数量
     */
    int countUserSkills(Long userId);

    /**
     * 检查用户是否已拥有某个技能
     *
     * @param userId 用户ID
     * @param skillTagId 技能标签ID
     * @return 存在返回1，否则返回0
     */
    int existsByUserIdAndSkillTagId(@Param("userId") Long userId, @Param("skillTagId") Long skillTagId);

    /**
     * 根据用户ID和技能分类查询用户技能
     *
     * @param userId 用户ID
     * @param category 技能分类
     * @return 用户技能列表
     */
    List<UserSkillVo> selectUserSkillsByUserIdAndCategory(@Param("userId") Long userId, @Param("category") String category);

    /**
     * 根据技能标签ID列表查询用户
     *
     * @param skillTagIds 技能标签ID列表
     * @return 用户技能列表
     */
    List<UserSkill> selectUsersBySkillTags(@Param("skillTagIds") List<Long> skillTagIds);

    /**
     * 查询用户技能及其得分
     *
     * @param userId 用户ID
     * @return 用户技能列表
     */
    List<UserSkillVo> selectUserSkillsWithScore(Long userId);
}