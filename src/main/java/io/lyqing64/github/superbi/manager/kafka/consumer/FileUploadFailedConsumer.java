package io.lyqing64.github.superbi.manager.kafka.consumer;

import io.lyqing64.github.superbi.constant.KafkaConstants;
import io.lyqing64.github.superbi.dto.FileUploadMessageDto;
import io.lyqing64.github.superbi.service.task.FileUploadTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FileUploadFailedConsumer {

    private final FileUploadTaskService fileUploadTaskService;

    public FileUploadFailedConsumer(FileUploadTaskService fileUploadTaskService) {
        this.fileUploadTaskService = fileUploadTaskService;
    }

    @KafkaListener(
            id = "fileUploadDlt",
            topics = KafkaConstants.FILE_UPLOAD_TOPIC_DLT,
            groupId = KafkaConstants.FILE_PARSE_DLT_GROUP,
            concurrency = "3"
    )
    public void listen(FileUploadMessageDto fileUploadMessageDto) {
        log.error("❌ 死信消息：{}", fileUploadMessageDto);
        try {
            fileUploadTaskService.parseFile(fileUploadMessageDto);
        } catch (Exception e) {
            log.error("❌ 重试失败：{}", e.getMessage());
        }
    }

}
