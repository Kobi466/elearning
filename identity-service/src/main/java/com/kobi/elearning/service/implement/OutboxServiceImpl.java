package com.kobi.elearning.service.implement;

import com.kobi.avro.UserCreatedEvent;
import com.kobi.avro.UserPayload;
import com.kobi.elearning.entity.OutboxEvent;
import com.kobi.elearning.entity.User;
import com.kobi.elearning.repository.OutboxEventRepository;
import com.kobi.elearning.service.OutboxEventService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class OutboxServiceImpl implements OutboxEventService {
    OutboxEventRepository outboxEventRepository;
    Serializer<Object> serializer;

    @Override
    public void saveOutboxEvent(
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

    @Override
    public UserCreatedEvent createdEvent(User user) {
        return UserCreatedEvent.newBuilder()
                .setEventId(UUID.randomUUID().toString())
                .setEventType("created")
                .setEventVersion(1)
                .setOccurredAt(Instant.now())
                .setCorrelationId(null)
                .setCausationId(UUID.randomUUID().toString())
                .setSource("identity-service")
                .setAggregateId(user.getUserId())
                .setUser(UserPayload.newBuilder()
                        .setUserId(user.getUserId())
                        .setEmail(user.getEmail())
                        .setUserName(user.getUserName())
                        .setCreatedAt(user.getCreatedAt())
                        .build()
                )
                .build();
    }
}
