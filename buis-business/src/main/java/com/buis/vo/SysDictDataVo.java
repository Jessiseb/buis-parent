package com.buis.vo;

import com.buis.common.annotation.Excel;
import lombok.Data;

@Data
public class SysDictDataVo {
    /** 字典标签 */
    @Excel(name = "字典标签")
    private String dictLabel;

    /** 字典键值 */
    @Excel(name = "字典键值")
    private String dictValue;

    /** 是否默认（Y是 N否） */
    @Excel(name = "是否默认", readConverterExp = "Y=是,N=否")
    private String isDefault;
}
