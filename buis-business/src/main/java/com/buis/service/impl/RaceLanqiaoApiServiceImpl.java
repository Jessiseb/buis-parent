package com.buis.service.impl;

import com.buis.service.IRaceLanqiaoApiService;
import com.buis.service.IRaceLanqiaoStudentService;
import com.buis.service.IRaceLanqiaoTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RaceLanqiaoApiServiceImpl implements IRaceLanqiaoApiService {

    @Autowired
    private IRaceLanqiaoStudentService raceLanqiaoStudentService;
    @Autowired
    private IRaceLanqiaoTeacherService raceLanqiaoTeacherService;

    @Override
    public boolean deleteLanqiaoStudentOrTeacherByUserId(Long loginId) {
        if(raceLanqiaoStudentService.selectRaceLanqiaoStudentByUserId(loginId) != null){
            raceLanqiaoStudentService.deleteRaceLanqiaoStudentByUserId(loginId);
            return true;
        }
        else if(raceLanqiaoTeacherService.selectRaceLanqiaoTeacherByUserId(loginId) != null){
            raceLanqiaoTeacherService.deleteRaceLanqiaoTeacherByUserId(loginId);
            return true;
        }

        throw new RuntimeException("用户不存在");
    }
}
