package com.buis.common.core.page;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

import static com.buis.common.utils.PageUtils.page;

/**
 * 分页数据对象
 * 
 * @author ruoyi
 */
@Data
public class MyPageVo<T> implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 总记录数 */
    private long total;

    /** 列表数据 */
    private T rows;

    /** 消息状态码 */
    private int code = 200;

    /** 消息内容 */
    private String msg = "成功";

    /**
     * 表格数据对象
     */
    public MyPageVo()
    {
    }

    /**
     * 分页
     * 
     * @param list 列表数据
     */
    public MyPageVo(T list)
    {
        this.rows = list;
        this.total = page.getTotal();
    }

    public long getTotal()
    {
        return total;
    }

    public void setTotal(long total)
    {
        this.total = total;
    }

    public T getRows()
    {
        return rows;
    }

    public void setRows(T rows)
    {
        this.rows = rows;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }
}
