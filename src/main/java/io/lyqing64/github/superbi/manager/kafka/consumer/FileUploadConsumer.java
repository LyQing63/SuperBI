package io.lyqing64.github.superbi.manager.kafka.consumer;

import io.lyqing64.github.superbi.constant.KafkaConstants;
import io.lyqing64.github.superbi.domain.FileUpload;
import io.lyqing64.github.superbi.dto.FileParseMessageDto;
import io.lyqing64.github.superbi.dto.FileUploadMessageDto;
import io.lyqing64.github.superbi.enums.TaskEventEnums;
import io.lyqing64.github.superbi.manager.kafka.producer.FileParserProducer;
import io.lyqing64.github.superbi.service.FileUploadService;
import io.lyqing64.github.superbi.service.impl.OssStorageService;
import io.lyqing64.github.superbi.service.task.TaskStatusService;
import io.lyqing64.github.superbi.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Slf4j
public class FileUploadConsumer {

    private final OssStorageService ossStorageService;
    private final FileUploadService fileUploadService;
    private final FileParserProducer fileParserProducer;
    private final TaskStatusService taskStatusService;

    public FileUploadConsumer(OssStorageService ossStorageService, FileUploadService fileUploadService, FileParserProducer fileParserProducer, TaskStatusService taskStatusService) {
        this.ossStorageService = ossStorageService;
        this.fileUploadService = fileUploadService;
        this.fileParserProducer = fileParserProducer;
        this.taskStatusService = taskStatusService;
    }

    @KafkaListener(
            id = "fileUpload",
            topics = KafkaConstants.FILE_UPLOAD_TOPIC,
            groupId = KafkaConstants.FILE_UPLOAD_GROUP,
            concurrency = "3"
    )
    public void listen(FileUploadMessageDto fileUploadMessageDto) {
        // 幂等判断
        // 获取文件下载路径
        FileUpload fileUpload = fileUploadService.getById(fileUploadMessageDto.getFileId());
        if (fileUpload == null) {
            log.error("文件获取失败: 文件不存在");
            taskStatusService.sendEvent(fileUploadMessageDto.getFileId(), TaskEventEnums.GENERATING_ERROR);
            return;
        }
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
            log.error("文件解析失败: {}", e.getMessage());
            taskStatusService.sendEvent(fileUploadMessageDto.getFileId(), TaskEventEnums.GENERATING_ERROR);
        }
        // 发送给模型
        FileParseMessageDto fileParseMessageDto = new FileParseMessageDto();
        fileParseMessageDto.setId(fileUploadMessageDto.getFileId());
        fileParseMessageDto.setDataSummary(csv);
        fileParserProducer.sendFileParserMessage(fileParseMessageDto);

        log.info("文件解析成功: taskId {}", fileUploadMessageDto.getFileId());
        taskStatusService.sendEvent(fileUploadMessageDto.getFileId(), TaskEventEnums.PARSING_SUCCESS);
    }

}
