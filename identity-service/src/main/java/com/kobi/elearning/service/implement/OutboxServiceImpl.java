package com.kobi.elearning.service.implement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kobi.elearning.entity.OutboxEvent;
import com.kobi.elearning.repository.OutboxEventRepository;
import com.kobi.elearning.service.OutboxEventService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class OutboxServiceImpl implements OutboxEventService {
    OutboxEventRepository outboxEventRepository;
    Serializer<Object> serializer;
//    ObjectMapper objectMapper;

    @Override
    public void saveOutboxEvent(
            String topic,
            String aggregateType,
            String aggregateId,
            String eventType,
            Object object,
            String correlationId,
            String source,
            String partitionKey
    ) {
        var payload = serializer.serialize(topic, object);
        outboxEventRepository.save(OutboxEvent.builder()
                .aggregateId(aggregateId)
                .aggregateType(aggregateType)
                .eventType(eventType)
                .payload(payload)
                .correlationId(correlationId)
                .source(source)
                .partitionKey(partitionKey)
                .build()

        );
        log.info("OUTBOX EVENT SAVED");
    }
}
