package com.buis.bo;

import lombok.Data;

/**
 * 队伍信息表返回队伍成员id,名称,学院,联系方式,学号
 */
@Data
public class MemberInfoBO {

    private Long id;
    private String name;
    private String sClass;
    private String college;
    private String phone;
    private String number;


}
