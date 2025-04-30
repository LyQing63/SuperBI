package io.lyqing64.github.superbi.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import io.lyqing64.github.superbi.common.BusinessCode;
import io.lyqing64.github.superbi.common.Response;
import io.lyqing64.github.superbi.domain.FileUpload;
import io.lyqing64.github.superbi.dto.FileUploadMessageDto;
import io.lyqing64.github.superbi.manager.kafka.producer.FileUploadProducer;
import io.lyqing64.github.superbi.service.FileService;
import io.lyqing64.github.superbi.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private OssStorageService ossStorageService; // 新增OSS服务注入

    @Autowired
    private FileUploadProducer fileUploadProducer; // 新增Kafka生产者注入

    @Override
    public Response<List<Map<Integer, String>>> uploadAndParseFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            log.error("上传文件为空");
            return Response.error(BusinessCode.UPLOAD_FILE_EMPTY_ERROR);
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !(originalFilename.endsWith(".xls") || originalFilename.endsWith(".xlsx"))) {
            log.error("上传文件格式不正确");
            return Response.error(BusinessCode.UPLOAD_FILE_TYPE_NOT_SUPPORTED);
        }

        try {
            // 读取文件内容到字节数组，保证流可复用
            byte[] fileBytes = file.getBytes();

            // 创建两个独立的输入流：一个用于上传OSS，一个用于后续解析
            try (InputStream inputStreamForUpload = new ByteArrayInputStream(fileBytes)) {

                // 上传文件到OSS并获取真实URL
                String ossUrl = ossStorageService.uploadFile(inputStreamForUpload, originalFilename);

                // 构建文件信息并保存到数据库
                FileUpload fileUpload = new FileUpload();
                fileUpload.setFileName(originalFilename);
                fileUpload.setFilePath(ossUrl); // 使用OSS真实URL
                fileUpload.setStatus("SUCCESS");

                // 保存文件元信息到数据库
                fileUploadService.save(fileUpload);

                // 新增：构造并发送Kafka消息
                FileUploadMessageDto messageDto = new FileUploadMessageDto();
                messageDto.setFileId(fileUpload.getId());
                // 如果需要幂等ID，可以从请求参数或其他来源获取
                fileUploadProducer.sendFileUploadMessage(messageDto);

            } catch (IOException e) {
                log.error("文件上传失败: {}", e.getMessage());
                return Response.error(BusinessCode.FILE_UPLOAD_FAILED);
            }

            // 使用新输入流解析文件
            try (InputStream inputStreamForParse = new ByteArrayInputStream(fileBytes)) {
                ExcelTypeEnum excelType = determineExcelType(originalFilename);

                List<Map<Integer, String>> dataList = EasyExcel.read(inputStreamForParse)
                        .excelType(excelType)
                        .sheet()
                        .headRowNumber(0)
                        .doReadSync();

                if (dataList == null || dataList.isEmpty()) {
                    log.error("解析Excel文件失败");
                    return Response.error(BusinessCode.FILE_PARSE_FAILED);
                }

                return Response.success(dataList);
            }
        } catch (IOException e) {
            log.error("文件解析失败: {}", e.getMessage());
            return Response.error(BusinessCode.FILE_PARSE_FAILED);
        } catch (Exception e) {
            log.error("文件处理异常: {}", e.getMessage());
            return Response.error(BusinessCode.FILE_PARSE_FAILED);
        }
    }

    private ExcelTypeEnum determineExcelType(String filename) {
        if (filename.endsWith(".xls")) {
            return ExcelTypeEnum.XLS;
        } else if (filename.endsWith(".xlsx")) {
            return ExcelTypeEnum.XLSX;
        }
        throw new IllegalArgumentException("Unsupported file type");
    }
}