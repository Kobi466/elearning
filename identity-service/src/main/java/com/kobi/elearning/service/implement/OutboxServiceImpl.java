package com.kobi.elearning.service.implement;

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

    @Override
    public void saveOutboxEvent(
            String id,
            String topic,
            String aggregateType,
            String aggregateId,
            String eventType,
            Object event,
            String correlationId,
            String source,
            String partitionKey
    ) {
        var payload = serializer.serialize(topic, event);
        outboxEventRepository.save(OutboxEvent.builder()
                .id(id)
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
