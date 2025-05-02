package io.lyqing64.github.superbi.manager.kafka.producer;

import io.lyqing64.github.superbi.constant.KafkaConstants;
import io.lyqing64.github.superbi.dto.FileParseMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FileParserProducer {

    private final KafkaTemplate<String, FileParseMessageDto> kafkaTemplate;

    public FileParserProducer(KafkaTemplate<String, FileParseMessageDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * 发送文件上传完成消息
     *
     * @param fileParseMessageDto 消息体
     */
    public void sendFileParserMessage(FileParseMessageDto fileParseMessageDto) {
        kafkaTemplate.send(KafkaConstants.FILE_PARSE_TOPIC, fileParseMessageDto)
                .whenComplete((res, ex) -> {
                    if (ex == null) {
                        log.info("文件csv消息发送成功!");
                    } else {
                        log.error("文件csv消息发送失败: {}", ex.getMessage());
                    }
                });
    }

}
