package io.lyqing64.github.superbi.dto;

import lombok.Data;

/**
 * 文件上传消息传输对象
 * 包含业务主键和幂等ID
 */
@Data
public class FileParseMessageDto {
    /**
     * 数据内容
     */
    private String dataSummary;

    /**
     * 数据库自增主键
     */
    private Long id;

    /**
     * 幂等标识（建议使用UUID）
     */
    private String correlationId;
}