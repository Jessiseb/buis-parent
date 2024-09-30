package com.ruoyi.businessmanager.userinfo.domain.entities;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户信息管理对象 user_info
 * 
 * @author zjx
 * @date 2024-10-01
 */
public class UserInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 学号或工号 */
    @Excel(name = "学号或工号")
    private String number;

    /** 密码 */
    @Excel(name = "密码")
    private String password;

    /** 邮箱 */
    @Excel(name = "邮箱")
    private String email;

    /** 手机号 */
    @Excel(name = "手机号")
    private String phone;

    /** 姓名 */
    @Excel(name = "姓名")
    private String name;

    /** 个人简介 */
    @Excel(name = "个人简介")
    private String description;

    /** 用户性别（字典） */
    @Excel(name = "用户性别", readConverterExp = "字=典")
    private Long sex;

    /** 学院 */
    @Excel(name = "学院")
    private String college;

    /** 专业 */
    @Excel(name = "专业")
    private String major;

    /** 老师职称（字典） */
    @Excel(name = "老师职称", readConverterExp = "字=典")
    private Long title;

    /** 禁用状态（字典） */
    @Excel(name = "禁用状态", readConverterExp = "字=典")
    private Long isDisabled;

    /** 标识是否被删除，0表示未删除 */
    @Excel(name = "标识是否被删除，0表示未删除")
    private Integer isDeleted;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updatedTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setNumber(String number) 
    {
        this.number = number;
    }

    public String getNumber() 
    {
        return number;
    }
    public void setPassword(String password) 
    {
        this.password = password;
    }

    public String getPassword() 
    {
        return password;
    }
    public void setEmail(String email) 
    {
        this.email = email;
    }

    public String getEmail() 
    {
        return email;
    }
    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }
    public void setSex(Long sex) 
    {
        this.sex = sex;
    }

    public Long getSex() 
    {
        return sex;
    }
    public void setCollege(String college) 
    {
        this.college = college;
    }

    public String getCollege() 
    {
        return college;
    }
    public void setMajor(String major) 
    {
        this.major = major;
    }

    public String getMajor() 
    {
        return major;
    }
    public void setTitle(Long title) 
    {
        this.title = title;
    }

    public Long getTitle() 
    {
        return title;
    }
    public void setIsDisabled(Long isDisabled) 
    {
        this.isDisabled = isDisabled;
    }

    public Long getIsDisabled() 
    {
        return isDisabled;
    }
    public void setIsDeleted(Integer isDeleted) 
    {
        this.isDeleted = isDeleted;
    }

    public Integer getIsDeleted() 
    {
        return isDeleted;
    }
    public void setCreatedTime(Date createdTime) 
    {
        this.createdTime = createdTime;
    }

    public Date getCreatedTime() 
    {
        return createdTime;
    }
    public void setUpdatedTime(Date updatedTime) 
    {
        this.updatedTime = updatedTime;
    }

    public Date getUpdatedTime() 
    {
        return updatedTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("number", getNumber())
            .append("password", getPassword())
            .append("email", getEmail())
            .append("phone", getPhone())
            .append("name", getName())
            .append("description", getDescription())
            .append("sex", getSex())
            .append("college", getCollege())
            .append("major", getMajor())
            .append("title", getTitle())
            .append("isDisabled", getIsDisabled())
            .append("isDeleted", getIsDeleted())
            .append("createdTime", getCreatedTime())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
