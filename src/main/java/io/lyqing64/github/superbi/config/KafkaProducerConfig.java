package io.lyqing64.github.superbi.config;

import io.lyqing64.github.superbi.constant.KafkaConstants;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

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

    @Bean
    public DeadLetterPublishingRecoverer recoverer(KafkaTemplate<Object, Object> template) {
        return new DeadLetterPublishingRecoverer(template,
                // 设置死信 topic 的命名规则（可自定义）
                (record, ex) -> new TopicPartition(record.topic() + ".DLT", record.partition())
        );
    }

    @Bean
    public DefaultErrorHandler errorHandler(DeadLetterPublishingRecoverer recoverer) {
        // 设置重试次数 + 间隔
        FixedBackOff backOff = new FixedBackOff(1000L, 3); // 每隔1s重试，最多3次
        DefaultErrorHandler handler = new DefaultErrorHandler(recoverer, backOff);

        // 你可以选择对某些异常做立即跳过、记录等处理
        return handler;
    }
}