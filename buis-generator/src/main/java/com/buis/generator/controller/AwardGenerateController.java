package com.buis.generator.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/award")
public class AwardGenerateController {

//    @Resource
//    private AwardGenerateService awardGenerateService;
//    @PostMapping("/generate")
//    public ResponseEntity<byte[]> generateCertificate(@RequestParam("teamId") Long teamId) {
//        // 1. 调用服务层生成 Word XML 内容
//        String xmlContent = awardGenerateService.generateAwardContent(teamId);
//
//        // 2. 将 XML 内容转换为字节数组
//        byte[] fileBytes = xmlContent.getBytes(StandardCharsets.UTF_8);
//
//        // 3. 设置响应头（文件下载）
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//        headers.setContentDisposition(ContentDisposition.builder("attachment")
//                .filename("certificates.docx")
//                .build());
//
//        return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
//    }
}
