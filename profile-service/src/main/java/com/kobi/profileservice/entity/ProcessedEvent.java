package com.kobi.profileservice.entity;

import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.time.Instant;

@Node("processed_event")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProcessedEvent {
    @Id
    @GeneratedValue
    private Long id;
    @Property
    private String eventId;
    @Property
    private Instant processedAt;
}

