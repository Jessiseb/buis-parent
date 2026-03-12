package com.buis.service.impl;

import java.util.List;
import com.buis.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.buis.mapper.CollegeMajorMapper;
import com.buis.model.domain.CollegeMajor;
import com.buis.service.ICollegeMajorService;

/**
 * 专业信息Service业务层处理
 * 
 * @author buis
 * @date 2024-10-10
 */
@Service
public class CollegeMajorServiceImpl implements ICollegeMajorService 
{
    @Autowired
    private CollegeMajorMapper collegeMajorMapper;

    /**
     * 查询专业信息
     * 
     * @param id 专业信息主键
     * @return 专业信息
     */
    @Override
    public CollegeMajor selectCollegeMajorById(Long id)
    {
        return collegeMajorMapper.selectCollegeMajorById(id);
    }

    /**
     * 查询专业信息列表
     * 
     * @param collegeMajor 专业信息
     * @return 专业信息
     */
    @Override
    public List<CollegeMajor> selectCollegeMajorList(CollegeMajor collegeMajor)
    {
        return collegeMajorMapper.selectCollegeMajorList(collegeMajor);
    }

    /**
     * 新增专业信息
     * 
     * @param collegeMajor 专业信息
     * @return 结果
     */
    @Override
    public int insertCollegeMajor(CollegeMajor collegeMajor)
    {
        collegeMajor.setCreateTime(DateUtils.getNowDate());
        return collegeMajorMapper.insertCollegeMajor(collegeMajor);
    }

    /**
     * 修改专业信息
     * 
     * @param collegeMajor 专业信息
     * @return 结果
     */
    @Override
    public int updateCollegeMajor(CollegeMajor collegeMajor)
    {
        collegeMajor.setUpdateTime(DateUtils.getNowDate());
        return collegeMajorMapper.updateCollegeMajor(collegeMajor);
    }

    /**
     * 批量删除专业信息
     * 
     * @param ids 需要删除的专业信息主键
     * @return 结果
     */
    @Override
    public int deleteCollegeMajorByIds(Long[] ids)
    {
        return collegeMajorMapper.deleteCollegeMajorByIds(ids);
    }

    /**
     * 删除专业信息信息
     * 
     * @param id 专业信息主键
     * @return 结果
     */
    @Override
    public int deleteCollegeMajorById(Long id)
    {
        return collegeMajorMapper.deleteCollegeMajorById(id);
    }
}
