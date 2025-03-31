package com.buis.bo;

import lombok.Data;

//团队指导老师信息
@Data
public class TeacherInfoBO {
    private Long id;
    private String name;
    private String college;
    private String phone;
    private String number;
    private String sex;
    //职称
    private String title;
}
