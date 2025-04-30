package io.lyqing64.github.superbi.common;

import lombok.Getter;

@Getter
public enum BusinessCode {
    UPLOAD_DATA_FORMAT_ERROR(40001, "上传数据格式不对"),
    FILE_UPLOAD_SUCCESS(20001, "文件上传成功"),
    FILE_PARSE_FAILED(40002, "文件解析失败"),
    AI_GENERATION_FAILED(40003, "AI生成失败"),
    AI_GENERATION_SUCCESS(20002, "AI生成成功");

    private final int code;
    private final String message;

    BusinessCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}