package com.buis.generator.util;

import com.buis.common.core.domain.R;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.poi.ss.formula.functions.T;

import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class AwardUtil {
    public R<String> generate(HttpServletResponse response, String basePackagePath
            , String templateFileName, Object templateParam, String fileName) {
        try {
            // 设置 HTTP 响应的内容类型为 Microsoft Word 文档
            response.setContentType("application/msword");
            // 设置响应字符编码为 UTF-8
            response.setCharacterEncoding("utf-8");
            // 使用 URLEncoder 对文件名进行编码，以防止中文文件名在不同浏览器和操作系统下出现乱码问题
            String filename = URLEncoder.encode(fileName + "_" + System.currentTimeMillis(), "utf-8");
            // 设置响应头，指定文档将以附件的形式下载，并定义文件名
            response.setHeader("Content-disposition", "attachment;filename=" + filename + ".docx");
            // 创建 Freemarker 的 Configuration 对象，设置默认的不兼容改进选项
            Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
            configuration.setDefaultEncoding("utf-8");
            // 设置模板加载器，加载模板文件
            configuration.setTemplateLoader(new ClassTemplateLoader(getClass(), basePackagePath));
            Template t = configuration.getTemplate(templateFileName, "utf-8");

            // 创建 Writer 对象，用于将生成的文档写到输出流中，缓存区大小设为 10KB
            Writer out = new BufferedWriter(
                    new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8),
                    10240);
            // 将模型数据与模板结合生成 Word 文档，写入到 Writer 对象中
            t.process(templateParam, out);

            out.close();
            return R.ok("生成奖状成功");
        } catch (Exception e) {
            throw new RuntimeException("生成奖状异常，异常原因：" + e.getMessage());
        }
    }

    public R<String> batchGenerate(HttpServletResponse response, String basePackagePath
            , String templateFileName, List<Map<String, String>> templateParams, String fileName) {
        try {
            // 设置ZIP压缩包响应头
            response.setContentType("application/zip");
            String filename = URLEncoder.encode(fileName + "_" + System.currentTimeMillis(), "utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + filename + ".zip");

            // 复用原有Freemarker配置
            Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
            configuration.setDefaultEncoding("utf-8");
            configuration.setTemplateLoader(new ClassTemplateLoader(getClass(), basePackagePath));
            Template t = configuration.getTemplate(templateFileName, "utf-8");

            try (ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())) {
                for (int i = 0; i < templateParams.size(); i++) {
                    Map<String, String> templateParam = templateParams.get(i);
                    // 生成每个文档的独立文件名
                    String entryName = fileName + "_" + (i + 1) + ".doc";
                    zos.putNextEntry(new ZipEntry(entryName));

                    // 复用原有模板处理逻辑
                    Writer out = new BufferedWriter(
                            new OutputStreamWriter(zos, StandardCharsets.UTF_8),
                            10240);
                    t.process(templateParam, out);
                    out.flush();
                    zos.closeEntry();
                }
            }
            return R.ok("批量生成成功");
        } catch (Exception e) {
            throw new RuntimeException("批量生成奖状异常，异常原因：" + e.getMessage(), e);
        }
    }
}



