package com.kobi.elearning.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kobi.elearning.entity.OutboxEvent;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, String> {
	List<OutboxEvent> findTop100ByStatusOrderByCreatedAtAsc(OutboxEvent.Status status);
}
