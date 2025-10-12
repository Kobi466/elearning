package com.kobi.event;

import org.springframework.context.ApplicationEvent;

public class OutboxEventCreatedEvent extends ApplicationEvent {
    public OutboxEventCreatedEvent(Object source) {
        super(source);
    }
}
