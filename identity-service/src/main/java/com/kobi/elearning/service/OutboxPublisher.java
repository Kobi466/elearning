package com.kobi.elearning.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kobi.avro.UserCreatedEvent;
import com.kobi.avro.UserPayload;
import com.kobi.elearning.entity.OutboxEvent;
import com.kobi.elearning.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class OutboxPublisher {
    OutboxEventRepository outboxEventRepository;
    KafkaTemplate<String, byte[]> kafkaTemplate;
    TransactionTemplate transactionTemplate;
    ObjectMapper objectMapper;
    Serializer<Object> serializer;
    @Scheduled(fixedDelay = 5000)
    public void pollingPublisher() {
        List<OutboxEvent> list = outboxEventRepository
                .findTop100ByStatusOrderByCreatedAtAsc(
                        OutboxEvent.Status.NEW
                );
        log.info("POLLING PUBLISHER SIZE {}", list.size());
        if (list.isEmpty()) {
            return;
        }
        for (OutboxEvent outboxEvent : list) {
            transactionTemplate.executeWithoutResult(transactionStatus -> {
                try {
//                    String topic = "user."
//                            + outboxEvent.getAggregateType() + "-"
//                            + outboxEvent.getEventType() + ".v"
//                            + outboxEvent.getVersion();
                    String topic = "user.user-created.v1"; // Hardcoded to match the consumer

                    log.info("Sending message to topic: {}", topic);
//                    var payload = objectMapper.readValue(outboxEvent.getPayload(), UserPayload.class);
//                    var avro = UserCreatedEvent.newBuilder()
//                            .setEventId(outboxEvent.getId())
//                            .setEventType(outboxEvent.getEventType())
//                            .setEventVersion(outboxEvent.getVersion())
//                            .setOccurredAt(outboxEvent.getCreatedAt())
//                            .setCorrelationId(outboxEvent.getCorrelationId())
//                            .setCausationId(outboxEvent.getId())
//                            .setSource(outboxEvent.getSource())
//                            .setAggregateId(outboxEvent.getAggregateId())
//                            .setUser(UserPayload.newBuilder()
//                                    .setUserId(outboxEvent.getAggregateId())
//                                    .setEmail(payload.getEmail())
//                                    .setUserName(payload.getUserName())
//                                    .setFullName(payload.getFullName())
//                                    .setCreatedAt(payload.getCreatedAt())
//                                    .setFirstName(payload.getFirstName())
//                                    .setLastName(payload.getLastName())
//                                    .setAvatar(payload.getAvatar())
//                                    .setLocale(payload.getLocale())
//                                    .build()
//                            )
//                            .build();
                    ProducerRecord<String, byte[]> record = new ProducerRecord<>(
                            topic,
                            outboxEvent.getAggregateId(),
                            outboxEvent.getPayload()
                    );
//                    record.headers().add("correlationId", outboxEvent.getCorrelationId().getBytes());
//                    record.headers().add("aggregateId", outboxEvent.getAggregateId().getBytes());
//                    record.headers().add("eventId", outboxEvent.getId().getBytes());
                    kafkaTemplate.send(record).get(10, TimeUnit.SECONDS);
                    outboxEvent.setStatus(OutboxEvent.Status.PUBLISHED);
                    outboxEvent.setPublishedAt(Instant.now());
                    log.info("PUBLISHED {}", outboxEvent);
                } catch (Exception e) {
                    outboxEvent.setLastError(e.getMessage());
                    outboxEvent.setRetryCount(outboxEvent.getRetryCount() + 1);
                    outboxEvent.setStatus(OutboxEvent.Status.FAILED);
                    log.error("PUBLISH FAILED {}", outboxEvent, e);
                }
                outboxEventRepository.save(outboxEvent);
            });
        }
    }

}

