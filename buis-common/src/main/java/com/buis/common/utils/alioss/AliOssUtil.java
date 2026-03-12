package com.buis.common.utils.alioss;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

@Data
@AllArgsConstructor
@Slf4j
@Component
public class AliOssUtil {
    @Autowired
    private AliOSSProperties aliOSSProperties;

    /**
     * 文件上传
     *
     * @param bytes
     * @param objectName
     * @return
     */
    public String upload(byte[] bytes, String objectName) {

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(aliOSSProperties.getEndpoint(), aliOSSProperties.getAccessKeyId(),aliOSSProperties.getAccessKeySecret());

        try {
            // 创建PutObject请求。
            ossClient.putObject(aliOSSProperties.getBucketName(), objectName, new ByteArrayInputStream(bytes));
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        //文件访问路径规则 https://BucketName.Endpoint/ObjectName
        StringBuilder stringBuilder = new StringBuilder("https://");
        stringBuilder
                .append(aliOSSProperties.getBucketName())
                .append(".")
                .append(aliOSSProperties.getEndpoint())
                .append("/")
                .append(objectName);

        log.info("文件上传到:{}", stringBuilder.toString());

        return stringBuilder.toString();
    }

    /**
     * 删除阿里云OSS上的指定文件
     * @param objectName OSS文件路径（例如：user-avatars/123-1699999999999.png）
     * @return true=删除成功；false=删除失败
     */
    public boolean delete(String objectName) {
        // 1. 创建OSS客户端（复用上传时的配置，从AliOSSProperties获取参数）
        OSS ossClient = new OSSClientBuilder().build(
                aliOSSProperties.getEndpoint(),
                aliOSSProperties.getAccessKeyId(),
                aliOSSProperties.getAccessKeySecret()
        );

        try {
            // 2. 执行删除操作（参数：bucketName + 要删除的文件路径）
            ossClient.deleteObject(aliOSSProperties.getBucketName(), objectName);
            log.info("OSS文件删除成功，路径：{}", objectName);
            return true; // 删除成功返回true
        } catch (OSSException oe) {
            // 处理OSS服务端异常（如文件不存在、权限不足等）
            log.error("OSS删除异常：文件路径={}, ErrorMessage={}, ErrorCode={}",
                    objectName, oe.getErrorMessage(), oe.getErrorCode());
            return false;
        } catch (ClientException ce) {
            // 处理客户端异常（如网络问题）
            log.error("OSS客户端异常：文件路径={}, ErrorMessage={}",
                    objectName, ce.getMessage());
            return false;
        } finally {
            // 3. 无论成功失败，都要关闭客户端释放资源
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
