package com.ruoyi.businessmanager.score.domain.entities;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 评委评分功能对象 t_score
 * 
 * @author zjx
 * @date 2024-10-01
 */
public class TScore extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 比赛 */
    @Excel(name = "比赛")
    private String race;

    /** 评委 */
    @Excel(name = "评委")
    private String teacher;

    /** 队伍 */
    @Excel(name = "队伍")
    private String team;

    /** 分数构成 */
    @Excel(name = "分数构成")
    private String build;

    /** 最终分数 */
    @Excel(name = "最终分数")
    private BigDecimal score;

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
    public void setRace(String race) 
    {
        this.race = race;
    }

    public String getRace() 
    {
        return race;
    }
    public void setTeacher(String teacher) 
    {
        this.teacher = teacher;
    }

    public String getTeacher() 
    {
        return teacher;
    }
    public void setTeam(String team) 
    {
        this.team = team;
    }

    public String getTeam() 
    {
        return team;
    }
    public void setBuild(String build) 
    {
        this.build = build;
    }

    public String getBuild() 
    {
        return build;
    }
    public void setScore(BigDecimal score) 
    {
        this.score = score;
    }

    public BigDecimal getScore() 
    {
        return score;
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
            .append("race", getRace())
            .append("teacher", getTeacher())
            .append("team", getTeam())
            .append("build", getBuild())
            .append("score", getScore())
            .append("isDeleted", getIsDeleted())
            .append("createdTime", getCreatedTime())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
