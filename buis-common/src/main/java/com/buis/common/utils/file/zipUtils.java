package com.buis.common.utils.file;

import cn.hutool.core.io.BufferUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.Buffer;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
@Slf4j
public class zipUtils {

    /**
     * 获取当前系统的临时目录
     */
    private static final String FILE_PATH = System.getProperty("java.io.tmpdir") + File.separator;

    public static final int BUFFER_SIZE = 1024 * 10;
//
//    /**
//     * 把文件集合打成zip压缩包
//     * @param srcFiles 压缩文件集合
//     * @param zipFile  zip文件名
//     * @throws RuntimeException 异常
//     */
//    public static void toZip(List<File> srcFiles, File zipFile) throws IOException {
//        if(zipFile == null){
//            return;
//        }
//        if(!zipFile.getName().endsWith(".zip")){
//            return;
//        }
//        ZipOutputStream zos = null;
//        FileOutputStream out = new FileOutputStream(zipFile);
//        try {
//            zos = new ZipOutputStream(out);
//            for (File srcFile : srcFiles) {
//                byte[] buf = new byte[BUFFER_SIZE];
//                zos.putNextEntry(new ZipEntry(srcFile.getName()));
//                int len;
//                // 读取文件并写入到zip中
//                FileInputStream in = new FileInputStream(srcFile);
//                while ((len = in.read(buf)) != -1) {
//                    zos.write(buf, 0, len);
//                    zos.flush();
//                }
//                in.close();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//            if (zos != null) {
//                zos.close();
//            }
//        }
//    }

    /**
     * zip打包下载
     *
     * @param response
     * @param zipFileName
     * @param fileList
     */
    public static void zipDownload(HttpServletResponse response, String zipFileName, List<File> fileList) {
        // zip文件路径
        String zipPath = FILE_PATH + zipFileName;
        try {
            //创建zip输出流
            try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipPath))) {
                //声明文件集合用于存放文件
                byte[] buffer = new byte[1024];
                //将文件放入zip压缩包
                for (int i = 0; i < fileList.size(); i++) {
                    File file = fileList.get(i);
                    try (FileInputStream fis = new FileInputStream(file)) {
                        out.putNextEntry(new ZipEntry(file.getName()));
                        int len;
                        // 读入需要下载的文件的内容，打包到zip文件
                        while ((len = fis.read(buffer)) > 0) {
                            out.write(buffer, 0, len);
                        }
                        out.closeEntry();
                    }
                }
            }
            //下载zip文件
            downFile(response, zipFileName);
        } catch (Exception e) {
            log.error("文件下载出错", e);
        } finally {
            // zip文件也删除
            fileList.add(new File(zipPath));
            deleteFile(fileList);
        }
    }



    /**
     * 文件下载
     *
     * @param response
     * @param zipFileName
     */
    private static void downFile(HttpServletResponse response, String zipFileName) {
        try {
            String path = FILE_PATH + zipFileName;
            File file = new File(path);
            if (file.exists()) {
                try (InputStream ins = new FileInputStream(path);
                     BufferedInputStream bins = new BufferedInputStream(ins);
                     OutputStream outs = response.getOutputStream();
                     BufferedOutputStream bouts = new BufferedOutputStream(outs)) {
                    response.setContentType("application/x-download");
                    response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(zipFileName, "UTF-8"));
                    int bytesRead = 0;
                    byte[] buffer = new byte[BUFFER_SIZE];
                    while ((bytesRead = bins.read(buffer, 0,BUFFER_SIZE)) != -1) {
                        bouts.write(buffer, 0, bytesRead);
                    }
                    bouts.flush();
                }
            }
        } catch (Exception e) {
            log .error("文件下载出错", e);
        }
    }

    /**
     * 删除文件
     *
     * @param fileList
     * @return
     */
    public static void deleteFile(List<File> fileList) {
        for (File file : fileList) {
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
