package io.lyqing64.github.superbi.manager.kafka.producer;

import io.lyqing64.github.superbi.constant.KafkaConstants;
import io.lyqing64.github.superbi.dto.FileUploadMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FileUploadProducer {

    private final KafkaTemplate<String, FileUploadMessageDto> kafkaTemplate;

    public FileUploadProducer(KafkaTemplate<String, FileUploadMessageDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * 发送文件上传完成消息（在数据库事务提交后发送）
     *
     * @param messageDto 消息体
     */
    public void sendFileUploadMessage(FileUploadMessageDto messageDto) {
        kafkaTemplate.send(KafkaConstants.FILE_UPLOAD_TOPIC, messageDto)
                .whenComplete((res, ex) -> {
                    if (ex == null) {
                        log.info("文件上传消息发送成功! fileId={}", messageDto.getFileId());
                    } else {
                        log.error("文件上传消息发送失败: {}", ex.getMessage());
                    }
                });
    }

}
