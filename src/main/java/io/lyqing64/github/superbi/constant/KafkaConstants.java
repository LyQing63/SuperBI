package io.lyqing64.github.superbi.constant;

public interface KafkaConstants {
    String FILE_UPLOAD_TOPIC = "super-bi.file.upload";  // 新增文件上传主题
    String FILE_UPLOAD_TOPIC_DLT = "super-bi.file.upload.DLT";
    String FILE_UPLOAD_GROUP = "super-bi.transfer.group";
    String FILE_UPLOAD_DLT_GROUP = "super-bi.dlt.transfer.group.";
    String FILE_PARSE_TOPIC = "super-bi.file.parse";
    String FILE_PARSE_TOPIC_DLT = "super-bi.file.parse.DLT";
    String FILE_PARSE_GROUP = "super-bi.parse.group";
    String FILE_PARSE_DLT_GROUP = "super-bi.dlt.parse.group";
}
