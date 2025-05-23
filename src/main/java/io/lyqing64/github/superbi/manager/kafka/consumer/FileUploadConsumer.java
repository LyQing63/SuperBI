package io.lyqing64.github.superbi.manager.kafka.consumer;

import io.lyqing64.github.superbi.constant.KafkaConstants;
import io.lyqing64.github.superbi.dto.FileUploadMessageDto;
import io.lyqing64.github.superbi.service.task.FileUploadTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FileUploadConsumer {

    private final FileUploadTaskService fileUploadTaskService;

    public FileUploadConsumer(FileUploadTaskService fileUploadTaskService) {
        this.fileUploadTaskService = fileUploadTaskService;
    }

    @KafkaListener(
            id = "fileUpload",
            topics = KafkaConstants.FILE_UPLOAD_TOPIC,
            groupId = KafkaConstants.FILE_UPLOAD_GROUP,
            concurrency = "3"
    )
    public void listen(FileUploadMessageDto fileUploadMessageDto) {
        try {
            fileUploadTaskService.parseFile(fileUploadMessageDto);
        } catch (Exception e) {
            // 状态更新
            fileUploadTaskService.doError(fileUploadMessageDto);
            // 进入私信队列
            log.error("文件上传失败：{}", e.getMessage());
        }
    }

}
