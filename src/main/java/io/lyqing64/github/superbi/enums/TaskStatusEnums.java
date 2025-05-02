package io.lyqing64.github.superbi.enums;

import lombok.Getter;

@Getter
public enum TaskStatusEnums {
    WAITING("WAITING", "任务等待中"),
    PARSING("PARSING", "数据转化中"),
    PARSING_ERROR("PARSING_ERROR", "数据转化失败"),
    GENERATING("GENERATING", "图像生成中"),
    GENERATING_ERROR("GENERATING_ERROR", "图像生成失败"),
    SUCCESS("SUCCESS", "成功");

    private final String code;
    private final String message;

    TaskStatusEnums(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
