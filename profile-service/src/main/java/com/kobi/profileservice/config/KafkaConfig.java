package com.kobi.profileservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConfig {

    @Value("${app.kafka.dlt.topic}")
    private String dltTopic;

    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<String, byte[]> template) {
        // Gửi tin nhắn lỗi vào DLT
        var recoverer = new DeadLetterPublishingRecoverer(template);
        // Cấu hình retry: thử lại 2 lần, mỗi lần cách nhau 1 giây
        var backOff = new FixedBackOff(1000L, 2);
        return new DefaultErrorHandler(recoverer, backOff);
    }
}