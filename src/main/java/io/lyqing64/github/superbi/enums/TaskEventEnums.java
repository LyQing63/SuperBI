package io.lyqing64.github.superbi.enums;

import lombok.Getter;

@Getter
public enum TaskEventEnums {
    // 上传阶段
    UPLOAD_COMPLETE("upload_complete", "上传完成"),

    // 解析阶段
    PARSING_SUCCESS("parsing_success", "解析成功"),
    PARSING_ERROR("parsing_error", "数据转化失败"),

    // 生成阶段
    GENERATING_SUCCESS("generating_success", "生成成功"),
    GENERATING_ERROR("generating_error", "图像生成失败");

    private final String code;
    private final String message;

    TaskEventEnums(String code, String message) {
        this.code = code;
        this.message = message;
    }
}