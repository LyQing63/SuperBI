package io.lyqing64.github.superbi.manager.kafka.producer;

import io.lyqing64.github.superbi.constant.KafkaConstants;
import io.lyqing64.github.superbi.dto.FileUploadMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

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
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            // 注册一个事务同步器，在事务提交后发送 Kafka 消息
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    doSend(messageDto);
                }
            });
        } else {
            // 如果没有事务，立即发送
            doSend(messageDto);
        }
    }

    /**
     * 实际发送 Kafka 消息的逻辑
     */
    private void doSend(FileUploadMessageDto messageDto) {
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
