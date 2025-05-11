package io.lyqing64.github.superbi.service.task;

import cn.hutool.core.lang.UUID;
import io.lyqing64.github.superbi.annotations.IdempotentTask;
import io.lyqing64.github.superbi.domain.FileUpload;
import io.lyqing64.github.superbi.dto.FileParseMessageDto;
import io.lyqing64.github.superbi.dto.FileUploadMessageDto;
import io.lyqing64.github.superbi.enums.TaskEventEnums;
import io.lyqing64.github.superbi.manager.kafka.producer.FileParserProducer;
import io.lyqing64.github.superbi.service.FileUploadService;
import io.lyqing64.github.superbi.service.impl.OssStorageService;
import io.lyqing64.github.superbi.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.io.File;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileUploadTaskService {

    private final OssStorageService ossStorageService;
    private final FileUploadService fileUploadService;
    private final FileParserProducer fileParserProducer;
    private final TaskStatusService taskStatusService;
    private final ApplicationEventPublisher eventPublisher;

    @IdempotentTask(key = "#fileUploadMessageDto.correlationId", timeoutSeconds = 600)
    @Transactional(rollbackFor = Exception.class)
    public void parseFile(FileUploadMessageDto fileUploadMessageDto) throws Exception {
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
        File file = ossStorageService.downloadFileToTmpFile(fileUpload.getFileName());
        csv = FileUtils.processExcelToCsvStr(file, fileUpload.getFileName());
        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                System.err.println("临时文件删除失败：" + file.getAbsolutePath());
            }
        }
        // 发送给模型
        FileParseMessageDto fileParseMessageDto = new FileParseMessageDto();
        fileParseMessageDto.setId(fileUploadMessageDto.getFileId());
        fileParseMessageDto.setDataSummary(csv);
        fileParseMessageDto.setCorrelationId(UUID.randomUUID().toString());

        eventPublisher.publishEvent(fileParseMessageDto);

        log.info("文件解析成功: taskId {}", fileUploadMessageDto.getFileId());
        taskStatusService.sendEvent(fileUploadMessageDto.getFileId(), TaskEventEnums.PARSING_SUCCESS);
    }

    public void doError(FileUploadMessageDto fileUploadMessageDto) {
        taskStatusService.sendEvent(fileUploadMessageDto.getFileId(), TaskEventEnums.PARSING_ERROR);
    }

    @TransactionalEventListener
    public void onTaskStatusChanged(FileParseMessageDto task) {
        fileParserProducer.sendFileParserMessage(task);
    }

}
