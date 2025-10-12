package com.kobi.paymentservice.service;

import com.kobi.event.OutboxEventCreatedEvent;
import com.kobi.paymentservice.entity.OutboxEvent;
import com.kobi.paymentservice.entity.enums.EventStatus;
import com.kobi.paymentservice.repository.OutboxEventRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OutboxEventPublisher{
    OutboxEventRepository outboxEventRepository;
    KafkaTemplate<String, String> kafkaTemplate;
    @NonFinal
    public static final String PAYMENT_EVENTS_TOPIC = "payment_events";

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleOutboxEventCreated(OutboxEventCreatedEvent event) {
        log.info("Nhận được tín hiệu có OutboxEvent mới, xử lý ngay lập tức...");
        var outboxEvent = event.getSource();
        this.publish((OutboxEvent) outboxEvent);
    }

    @Scheduled(fixedRate = 500000)
    @Transactional
    public void processStaleOutboxEvents(){
        List<OutboxEvent> staleEvents = outboxEventRepository.findOutboxEventByStatus(EventStatus.NEW);
        if (staleEvents.isEmpty()) {
            log.info("Không có event nào bị kẹt.");
            return;
        }
        log.warn("Phát hiện {} event bị kẹt. Bắt đầu gửi lại...", staleEvents.size());
        for (OutboxEvent outboxEvent : staleEvents) {
            this.publish(outboxEvent);
            log.info("Đã gửi thành công event {} lên Kafka topic.", outboxEvent.getPayload());
        }
    }

    private void publish(OutboxEvent outboxEvent){
        try{
            kafkaTemplate.send(PAYMENT_EVENTS_TOPIC, outboxEvent.getPayload());
            log.info("Đã gửi thành công event {} lên Kafka topic {}.", outboxEvent.getId(), PAYMENT_EVENTS_TOPIC);
            outboxEvent.setStatus(EventStatus.PUBLISH);
            outboxEventRepository.save(outboxEvent);
            log.info("Đã publish thanh công event");
        } catch (Exception ignored){
            log.error("Lỗi nghiêm trọng khi gửi event {}: {}", outboxEvent.getId(), ignored.getMessage());
            outboxEvent.setStatus(EventStatus.FAILED);
            outboxEventRepository.save(outboxEvent);
        }
    }
}

