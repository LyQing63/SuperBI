package io.lyqing64.github.superbi.dto;

import lombok.Data;

/**
 * 文件上传消息传输对象
 * 包含业务主键和幂等ID
 */
@Data
public class FileUploadMessageDto {
    /**
     * 数据库自增主键
     */
    private Long fileId;

    /**
     * 幂等标识（建议使用UUID）
     */
    private String correlationId;
}