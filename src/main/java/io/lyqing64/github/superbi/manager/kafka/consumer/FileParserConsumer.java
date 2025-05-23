package io.lyqing64.github.superbi.manager.kafka.consumer;

import io.lyqing64.github.superbi.constant.KafkaConstants;
import io.lyqing64.github.superbi.dto.FileParseMessageDto;
import io.lyqing64.github.superbi.service.task.FileParserTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FileParserConsumer {

    private final FileParserTaskService fileParserTaskService;

    public FileParserConsumer(FileParserTaskService fileParserTaskService) {
        this.fileParserTaskService = fileParserTaskService;
    }

    @KafkaListener(
            id = "fileParse",
            topics = KafkaConstants.FILE_PARSE_TOPIC,
            groupId = KafkaConstants.FILE_PARSE_GROUP,
            concurrency = "3"
    )
    public void listen(FileParseMessageDto fileParseMessageDto) {

        try {
            fileParserTaskService.parseFile(fileParseMessageDto);
        } catch (Exception e) {
            // 状态更新
            fileParserTaskService.doError(fileParseMessageDto);
            // 进入私信队列
            log.error("图像生成失败：{}", e.getMessage());
        }
    }

}
