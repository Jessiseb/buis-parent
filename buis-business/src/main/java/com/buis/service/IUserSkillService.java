package com.buis.service;

import com.buis.model.domain.UserSkill;
import com.buis.model.dto.SkillExtractRequest;
import com.buis.model.dto.SkillExtractResult;
import com.buis.model.vo.UserSkillVo;

import java.util.List;

/**
 * 用户技能服务接口
 *
 *
 */
public interface IUserSkillService {

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
    int batchInsertUserSkill(List<UserSkill> userSkillList);

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
    int deleteUserSkillByUserIdAndSkillTagId(Long userId, Long skillTagId);

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
     * 更新用户的技能标签
     *
     * @param userId 用户ID
     * @param skillTagIds 技能标签ID列表
     * @return 结果
     */
    int updateUserSkills(Long userId, List<Long> skillTagIds);

    /**
     * 使用AI提取并关联用户技能
     *
     * @param userId 用户ID
     * @param request 技能提取请求
     * @return 技能提取结果
     */
    SkillExtractResult extractAndAddUserSkills(Long userId, SkillExtractRequest request);

    /**
     * 检查用户是否已拥有某个技能
     *
     * @param userId 用户ID
     * @param skillTagId 技能标签ID
     * @return 是否拥有
     */
    boolean hasSkill(Long userId, Long skillTagId);
}