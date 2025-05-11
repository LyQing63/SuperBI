package io.lyqing64.github.superbi.service.task;

import io.lyqing64.github.superbi.annotations.IdempotentTask;
import io.lyqing64.github.superbi.domain.ChartGenerate;
import io.lyqing64.github.superbi.dto.FileParseMessageDto;
import io.lyqing64.github.superbi.enums.TaskEventEnums;
import io.lyqing64.github.superbi.response.ChartGeneratorResponse;
import io.lyqing64.github.superbi.service.AiService;
import io.lyqing64.github.superbi.service.ChartGenerateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class FileParserTaskService {

    private final AiService aiService;
    private final ChartGenerateService chartGenerateService;
    private final TaskStatusService taskStatusService;

    public FileParserTaskService(AiService aiService, ChartGenerateService chartGenerateService, TaskStatusService taskStatusService) {
        this.aiService = aiService;
        this.chartGenerateService = chartGenerateService;
        this.taskStatusService = taskStatusService;
    }

    @IdempotentTask(key = "#fileParseMessageDto.correlationId", timeoutSeconds = 600)
    @Transactional(rollbackFor = Exception.class)
    public void parseFile(FileParseMessageDto fileParseMessageDto) throws Exception {
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

    }

    public void doError(FileParseMessageDto fileParseMessageDto) {
        taskStatusService.sendEvent(fileParseMessageDto.getId(), TaskEventEnums.GENERATING_ERROR);
    }

}
