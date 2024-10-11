package com.buis.service;

import java.util.List;
import com.buis.domain.CollegeMajor;

/**
 * 专业信息Service接口
 * 
 * @author buis
 * @date 2024-10-10
 */
public interface ICollegeMajorService 
{
    /**
     * 查询专业信息
     * 
     * @param id 专业信息主键
     * @return 专业信息
     */
    public CollegeMajor selectCollegeMajorById(Long id);

    /**
     * 查询专业信息列表
     * 
     * @param collegeMajor 专业信息
     * @return 专业信息集合
     */
    public List<CollegeMajor> selectCollegeMajorList(CollegeMajor collegeMajor);

    /**
     * 新增专业信息
     * 
     * @param collegeMajor 专业信息
     * @return 结果
     */
    public int insertCollegeMajor(CollegeMajor collegeMajor);

    /**
     * 修改专业信息
     * 
     * @param collegeMajor 专业信息
     * @return 结果
     */
    public int updateCollegeMajor(CollegeMajor collegeMajor);

    /**
     * 批量删除专业信息
     * 
     * @param ids 需要删除的专业信息主键集合
     * @return 结果
     */
    public int deleteCollegeMajorByIds(Long[] ids);

    /**
     * 删除专业信息信息
     * 
     * @param id 专业信息主键
     * @return 结果
     */
    public int deleteCollegeMajorById(Long id);
}
