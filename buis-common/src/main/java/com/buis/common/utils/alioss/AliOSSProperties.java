package com.buis.common.utils.alioss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss")//yml前缀
public class AliOSSProperties {
    private String endpoint;
    private String bucketName;
    private String accessKeyId;
    private String accessKeySecret;
}
