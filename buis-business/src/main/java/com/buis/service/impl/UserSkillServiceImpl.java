package com.buis.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.buis.common.exception.ServiceException;
import com.buis.mapper.SkillTagMapper;
import com.buis.mapper.UserSkillMapper;
import com.buis.model.domain.SkillTag;
import com.buis.model.domain.UserSkill;
import com.buis.model.dto.SkillExtractRequest;
import com.buis.model.dto.SkillExtractResult;
import com.buis.model.vo.UserSkillVo;
import com.buis.service.IAiService;
import com.buis.service.IUserSkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户技能服务实现
 *
 * @author buis
 */
@Service
public class UserSkillServiceImpl implements IUserSkillService {

    @Autowired
    private UserSkillMapper userSkillMapper;

    @Autowired
    private SkillTagMapper skillTagMapper;

    @Autowired
    private IAiService aiService;

    /**
     * 查询用户技能关联
     *
     * @param id 用户技能关联主键
     * @return 用户技能关联
     */
    @Override
    public UserSkill selectUserSkillById(Long id) {
        return userSkillMapper.selectUserSkillById(id);
    }

    /**
     * 查询用户技能关联列表
     *
     * @param userSkill 用户技能关联
     * @return 用户技能关联集合
     */
    @Override
    public List<UserSkillVo> selectUserSkillList(UserSkill userSkill) {
        return userSkillMapper.selectUserSkillList(userSkill);
    }

    /**
     * 根据用户ID查询用户技能列表
     *
     * @param userId 用户ID
     * @return 用户技能列表
     */
    @Override
    public List<UserSkillVo> selectUserSkillsByUserId(Long userId) {
        return userSkillMapper.selectUserSkillsByUserId(userId);
    }

    /**
     * 新增用户技能关联
     *
     * @param userSkill 用户技能关联
     * @return 结果
     */
    @Override
    public int insertUserSkill(UserSkill userSkill) {
        // 检查技能标签是否存在
        SkillTag skillTag = skillTagMapper.selectSkillTagById(userSkill.getSkillTagId());
        if (skillTag == null || skillTag.getIsActive() == 0) {
            throw new ServiceException("技能标签不存在或已禁用");
        }

        // 检查是否已关联
        int exists = userSkillMapper.existsByUserIdAndSkillTagId(userSkill.getUserId(), userSkill.getSkillTagId());
        if (exists > 0) {
            throw new ServiceException("该技能已关联");
        }

        // 设置默认值
        if (userSkill.getSource() == null) {
            userSkill.setSource("manual");
        }
        if (userSkill.getConfidence() == null) {
            userSkill.setConfidence(new BigDecimal("1.00"));
        }
        if (userSkill.getCreateTime() == null) {
            userSkill.setCreateTime(new Date());
        }

        return userSkillMapper.insertUserSkill(userSkill);
    }

    /**
     * 批量新增用户技能关联
     *
     * @param userSkillList 用户技能关联列表
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsertUserSkill(List<UserSkill> userSkillList) {
        if (CollectionUtils.isEmpty(userSkillList)) {
            return 0;
        }

        // 过滤掉已存在的关联
        List<UserSkill> toInsert = new ArrayList<>();
        Date now = new Date();

        for (UserSkill userSkill : userSkillList) {
            int exists = userSkillMapper.existsByUserIdAndSkillTagId(userSkill.getUserId(), userSkill.getSkillTagId());
            if (exists == 0) {
                // 设置默认值
                if (userSkill.getSource() == null) {
                    userSkill.setSource("manual");
                }
                if (userSkill.getConfidence() == null) {
                    userSkill.setConfidence(new BigDecimal("1.00"));
                }
                userSkill.setCreateTime(now);
                toInsert.add(userSkill);
            }
        }

        if (CollectionUtils.isEmpty(toInsert)) {
            return 0;
        }

        return userSkillMapper.batchInsertUserSkill(toInsert);
    }

    /**
     * 修改用户技能关联
     *
     * @param userSkill 用户技能关联
     * @return 结果
     */
    @Override
    public int updateUserSkill(UserSkill userSkill) {
        // 检查记录是否存在
        UserSkill existing = userSkillMapper.selectUserSkillById(userSkill.getId());
        if (existing == null) {
            throw new ServiceException("用户技能关联不存在");
        }

        return userSkillMapper.updateUserSkill(userSkill);
    }

    /**
     * 删除用户技能关联
     *
     * @param id 用户技能关联主键
     * @return 结果
     */
    @Override
    public int deleteUserSkillById(Long id) {
        return userSkillMapper.deleteUserSkillById(id);
    }

    /**
     * 根据用户ID和技能标签ID删除用户技能关联
     *
     * @param userId 用户ID
     * @param skillTagId 技能标签ID
     * @return 结果
     */
    @Override
    public int deleteUserSkillByUserIdAndSkillTagId(Long userId, Long skillTagId) {
        return userSkillMapper.deleteUserSkillByUserIdAndSkillTagId(userId, skillTagId);
    }

    /**
     * 批量删除用户技能关联
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    @Override
    public int deleteUserSkillByIds(Long[] ids) {
        return userSkillMapper.deleteUserSkillByIds(ids);
    }

    /**
     * 根据用户ID删除该用户的所有技能关联
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public int deleteUserSkillsByUserId(Long userId) {
        return userSkillMapper.deleteUserSkillsByUserId(userId);
    }

    /**
     * 更新用户的技能标签
     *
     * @param userId 用户ID
     * @param skillTagIds 技能标签ID列表
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateUserSkills(Long userId, List<Long> skillTagIds) {
        if (userId == null) {
            throw new ServiceException("用户ID不能为空");
        }

        // 删除用户原有的技能关联
        userSkillMapper.deleteUserSkillsByUserId(userId);

        if (CollectionUtils.isEmpty(skillTagIds)) {
            return 0;
        }

        // 构建新的用户技能关联列表
        List<UserSkill> userSkills = new ArrayList<>();
        Date now = new Date();

        for (Long skillTagId : skillTagIds) {
            UserSkill userSkill = new UserSkill();
            userSkill.setUserId(userId);
            userSkill.setSkillTagId(skillTagId);
            userSkill.setSource("manual");
            userSkill.setConfidence(new BigDecimal("1.00"));
            userSkill.setCreateTime(now);
            userSkills.add(userSkill);
        }

        // 批量插入
        return batchInsertUserSkill(userSkills);
    }

    /**
     * 使用AI提取并关联用户技能
     *
     * @param userId 用户ID
     * @param request 技能提取请求
     * @return 技能提取结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SkillExtractResult extractAndAddUserSkills(Long userId, SkillExtractRequest request) {
        if (userId == null) {
            throw new ServiceException("用户ID不能为空");
        }

        // 调用AI提取技能
        SkillExtractResult extractResult = aiService.extractSkillsFromText(request);

        // 如果没有匹配到技能，直接返回
        if (CollectionUtils.isEmpty(extractResult.getMatchedSkills())) {
            return extractResult;
        }

        // 构建用户技能关联列表
        List<UserSkill> userSkills = new ArrayList<>();
        Date now = new Date();

        for (SkillTag skillTag : extractResult.getMatchedSkills()) {
            // 检查是否已存在该关联
            int exists = userSkillMapper.existsByUserIdAndSkillTagId(userId, skillTag.getId());
            if (exists == 0) {
                // 使用传统方式创建对象
                UserSkill userSkill = new UserSkill();
                userSkill.setUserId(userId);
                userSkill.setSkillTagId(skillTag.getId());
                userSkill.setSource("ai");
                userSkill.setConfidence(new BigDecimal("0.85")); // AI提取的置信度
                userSkill.setCreateTime(now);
                userSkills.add(userSkill);
            }
        }

        // 批量插入
        if (!CollectionUtils.isEmpty(userSkills)) {
            batchInsertUserSkill(userSkills);
        }

        return extractResult;
    }

    /**
     * 检查用户是否已拥有某个技能
     *
     * @param userId 用户ID
     * @param skillTagId 技能标签ID
     * @return 是否拥有
     */
    @Override
    public boolean hasSkill(Long userId, Long skillTagId) {
        int exists = userSkillMapper.existsByUserIdAndSkillTagId(userId, skillTagId);
        return exists > 0;
    }
}