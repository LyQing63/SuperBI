package io.lyqing64.github.superbi.utils;

import io.lyqing64.github.superbi.domain.FileUpload;
import io.lyqing64.github.superbi.service.FileUploadService;
import io.lyqing64.github.superbi.service.impl.OssStorageService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@SpringBootTest
@Slf4j
public class easyExcelTest {

    @Autowired
    private OssStorageService ossStorageService;
    @Autowired
    private FileUploadService fileUploadService;

    @BeforeAll
    static void loadEnv() {
        EnvLoader.loadEnvToSystem();
    }

    @Test
    public void test() {

        // 获取文件下载路径
        FileUpload fileUpload = fileUploadService.getById(15L);
        // 下载文件
        // 解析成csv
        String csv = null;
        try {
            File file = ossStorageService.downloadFileToTmpFile(fileUpload.getFileName());
            csv = FileUtils.processExcelToCsvStr(file, fileUpload.getFileName());
            if (file.exists()) {
                boolean deleted = file.delete();
                if (!deleted) {
                    System.err.println("临时文件删除失败：" + file.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            File tmp = new File(System.getProperty("java.io.tmpdir"));
            log.error("临时目录: {}, 可写: {}", tmp.getAbsolutePath(), tmp.canWrite());
            log.error("当前临时目录：{}", System.getProperty("java.io.tmpdir"));
            log.error("文件解析失败: {}", e.getMessage());
        }
        System.out.println("res : " + csv);
    }

}
