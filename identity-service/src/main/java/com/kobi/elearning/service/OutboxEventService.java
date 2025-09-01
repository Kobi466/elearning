package com.kobi.elearning.service;

import com.kobi.avro.UserCreatedEvent;
import com.kobi.elearning.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface OutboxEventService {
    void saveOutboxEvent(String topic
            , String aggregateType
            , String aggregateId
            , String eventType
            , Object event
            , String source
            , String correlationId
            , String partitionKey
    );//correlationId is not needed
    UserCreatedEvent createdEvent(User user);
}
