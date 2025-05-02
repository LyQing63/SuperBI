package io.lyqing64.github.superbi.manager.kafka.consumer;

import io.lyqing64.github.superbi.constant.KafkaConstants;
import io.lyqing64.github.superbi.domain.ChartGenerate;
import io.lyqing64.github.superbi.dto.FileParseMessageDto;
import io.lyqing64.github.superbi.enums.TaskEventEnums;
import io.lyqing64.github.superbi.response.ChartGeneratorResponse;
import io.lyqing64.github.superbi.service.AiService;
import io.lyqing64.github.superbi.service.ChartGenerateService;
import io.lyqing64.github.superbi.service.task.TaskStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FileParserConsumer {

    private final AiService aiService;
    private final ChartGenerateService chartGenerateService;
    private final TaskStatusService taskStatusService;

    public FileParserConsumer(AiService aiService, ChartGenerateService chartGenerateService, TaskStatusService taskStatusService) {
        this.aiService = aiService;
        this.chartGenerateService = chartGenerateService;
        this.taskStatusService = taskStatusService;
    }

    @KafkaListener(
            id = "fileParse",
            topics = KafkaConstants.FILE_PARSE_TOPIC,
            groupId = KafkaConstants.FILE_PARSE_GROUP,
            concurrency = "3"
    )
    public void listen(FileParseMessageDto fileParseMessageDto) {
        // 幂等判断
        // 让大模型解析
        try {
            ChartGeneratorResponse assistant = (ChartGeneratorResponse) aiService.assistant(fileParseMessageDto.getDataSummary());
            log.info("assistant result: {}", assistant);
            // 结果入库存储
            ChartGenerate chartGenerate = new ChartGenerate();
            chartGenerate.setModelUsed("Gemini");
            chartGenerate.setFileId(fileParseMessageDto.getId());
            chartGenerate.setChartJson(assistant.getEchartCode());
            chartGenerate.setAnalysis(assistant.getAnalysis());
            chartGenerateService.save(chartGenerate);
            taskStatusService.sendEvent(fileParseMessageDto.getId(), TaskEventEnums.GENERATING_SUCCESS);
        } catch (Exception e) {
            log.error("大模型解析失败: {}", e.getMessage());
            taskStatusService.sendEvent(fileParseMessageDto.getId(), TaskEventEnums.GENERATING_ERROR);
        }

    }

}
