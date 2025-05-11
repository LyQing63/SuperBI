package io.lyqing64.github.superbi.manager.kafka.consumer;

import io.lyqing64.github.superbi.constant.KafkaConstants;
import io.lyqing64.github.superbi.dto.FileParseMessageDto;
import io.lyqing64.github.superbi.service.task.FileParserTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FileParserFailedConsumer {

    private final FileParserTaskService fileParserTaskService;

    public FileParserFailedConsumer(FileParserTaskService fileParserTaskService) {
        this.fileParserTaskService = fileParserTaskService;
    }

    @KafkaListener(
            id = "fileParseDlt",
            topics = KafkaConstants.FILE_PARSE_TOPIC_DLT,
            groupId = KafkaConstants.FILE_PARSE_DLT_GROUP,
            concurrency = "3"
    )
    public void listen(FileParseMessageDto fileParseMessageDto) {
        log.error("❌ 死信消息：{}", fileParseMessageDto);
        try {
            fileParserTaskService.parseFile(fileParseMessageDto);
        } catch (Exception e) {
            log.error("❌ 重试失败：{}", e.getMessage());
        }
    }

}
