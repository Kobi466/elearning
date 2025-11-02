package com.kobi.elearning.service;


import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import com.kobi.elearning.entity.OutboxEvent;
import com.kobi.elearning.repository.OutboxEventRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class OutboxPublisher {
	OutboxEventRepository outboxEventRepository;
	KafkaTemplate<String, byte[]> kafkaTemplate;
	TransactionTemplate transactionTemplate;

	@Scheduled(fixedDelay = 10000)
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
					String topic = "user."
							+ outboxEvent.getAggregateType() + "-"
							+ outboxEvent.getEventType() + ".v"
							+ outboxEvent.getVersion();
					log.info("Sending message to topic: {}", topic);
//
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
