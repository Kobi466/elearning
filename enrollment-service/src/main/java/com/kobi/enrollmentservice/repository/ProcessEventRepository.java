package com.kobi.enrollmentservice.repository;

import com.kobi.enrollmentservice.entity.ProcessEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessEventRepository extends JpaRepository<ProcessEvent,String> {
    boolean existsByEventId(String eventId);
}
