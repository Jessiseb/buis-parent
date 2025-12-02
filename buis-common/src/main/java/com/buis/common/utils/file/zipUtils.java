package com.buis.common.utils.file;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
public class zipUtils {

    /**
     * 获取当前系统的临时目录
     */
    private static final String FILE_PATH = System.getProperty("java.io.tmpdir") + File.separator;

    public static final int BUFFER_SIZE = 1024 * 10;

    public static void addToZipFile(String fileUrl, ZipOutputStream zos) throws IOException {
        try (FileInputStream fis = new FileInputStream(fileUrl)) {
            ZipEntry zipEntry = new ZipEntry(new File(fileUrl).getName());
            zos.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zos.write(bytes, 0, length);
            }
        }
    }


    public static void addToZipFileFromUrl(String fileUrl, ZipOutputStream zos) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(fileUrl);

        try (CloseableHttpResponse response = httpClient.execute(httpGet);
             InputStream inputStream = response.getEntity().getContent()) {

            // 从URL提取文件名
            String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            ZipEntry zipEntry = new ZipEntry(fileName);
            zos.putNextEntry(zipEntry);

            byte[] buffer = new byte[BUFFER_SIZE];
            int len;
            while ((len = inputStream.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }
            zos.closeEntry();
        } finally {
            httpClient.close();
        }
    }
}
