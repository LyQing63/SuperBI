package io.lyqing64.github.superbi.constant;

public interface KafkaConstants {
    String FILE_UPLOAD_TOPIC = "super-bi.file.upload";  // 新增文件上传主题
    String FILE_UPLOAD_GROUP = "super-bi.transfer.group";
    String FILE_PARSE_TOPIC = "super-bi.file.parse";
    String FILE_PARSE_GROUP = "super-bi.parse.group";
}
