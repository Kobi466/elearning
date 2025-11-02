package com.kobi.profileservice.repository;

import com.kobi.profileservice.entity.ProcessedEvent;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessedEventRepository extends Neo4jRepository<ProcessedEvent, Long> {
    boolean existsByEventId(String eventId);
}
