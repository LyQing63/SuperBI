package io.lyqing64.github.superbi.config;

import io.lyqing64.github.superbi.constant.KafkaConstants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaProducerConfig {

    @Bean  // 新增文件上传主题配置
    public NewTopic fileUploadTopic() {
        return TopicBuilder.name(KafkaConstants.FILE_UPLOAD_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean  // 新增文件上传主题配置
    public NewTopic fileParserTopic() {
        return TopicBuilder.name(KafkaConstants.FILE_PARSE_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }
}