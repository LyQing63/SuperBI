package io.lyqing64.github.superbi.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class OssStorageService {

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    /**
     * 上传文件到OSS
     *
     * @param inputStream 文件输入流
     * @param objectName  文件在OSS中的名称
     * @return 文件访问URL
     */
    public String uploadFile(InputStream inputStream, String objectName) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            // 上传文件
            ossClient.putObject(new PutObjectRequest(bucketName, objectName, inputStream));
            // 返回文件访问URL
            return "https://" + bucketName + "." + endpoint + "/" + objectName;
        } finally {
            // 关闭OSS客户端
            ossClient.shutdown();
        }
    }

    /**
     * 下载OSS中的文件
     *
     * @param objectName 文件在OSS中的名称
     * @return 文件输入流
     */
    public InputStream downloadFile(String objectName) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            OSSObject ossObject = ossClient.getObject(bucketName, objectName);
            return ossObject.getObjectContent();
        } finally {
            ossClient.shutdown();
        }
    }

    public File downloadFileToTmpFile(String objectName) throws IOException {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            OSSObject ossObject = ossClient.getObject(bucketName, objectName);
            return saveToTempFile(ossObject.getObjectContent(), objectName);
        } finally {
            ossClient.shutdown();
        }
    }

    /**
     * 将 InputStream 保存为临时文件
     */
    public File saveToTempFile(InputStream inputStream, String filename) throws IOException {
        File tempDir = new File("target/tmp");
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }

        File tempFile = new File(tempDir, filename);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[8192];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
        }
        return tempFile;
    }

    /**
     * 删除OSS中的文件
     *
     * @param objectName 文件在OSS中的名称
     */
    public void deleteFile(String objectName) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            ossClient.deleteObject(bucketName, objectName);
        } finally {
            ossClient.shutdown();
        }
    }
}