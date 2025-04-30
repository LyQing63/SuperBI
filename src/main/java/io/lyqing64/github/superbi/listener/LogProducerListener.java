package io.lyqing64.github.superbi.listener;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

@Component
public class LogProducerListener<K, V> implements ProducerListener<K, V> {
    /**
     * todo 日志系统存储
     *
     * @param producerRecord
     * @param recordMetadata
     */

    @Override
    public void onSuccess(ProducerRecord<K, V> producerRecord, RecordMetadata recordMetadata) {
        ProducerListener.super.onSuccess(producerRecord, recordMetadata);
    }

    @Override
    public void onError(ProducerRecord<K, V> producerRecord, RecordMetadata recordMetadata, Exception exception) {
        ProducerListener.super.onError(producerRecord, recordMetadata, exception);
    }
}
