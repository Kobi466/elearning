package com.kobi.profileservice.service;

import com.kobi.avro.UserCreatedEvent;
import com.kobi.avro.UserPayload;
import com.kobi.profileservice.entity.ProcessedEvent;
import com.kobi.profileservice.repository.ProcessedEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ProcessedEventService {
    ProcessedEventRepository processedEventRepository;
    ProfileService profileService;

    @Transactional
    public void processEvent(UserCreatedEvent event) {
//        if (processedEventRepository.existsByEventId(event.getEventId())){
//            log.info("Event already processed {}", event.getEventId());
//            return;
//        }
//        profileService.createProfileFromPayload(event.getUser());
//
//        processedEventRepository.save(
//                ProcessedEvent.builder()
//                        .eventId(event.getEventId())
//                        .processedAt(Instant.now())
//                        .build()
//        );
//        log.info("Event processed {}", event.getEventId());
        System.out.println("DEBUG: Intentionally throwing an exception to test the Saga unhappy path.");
        throw new RuntimeException("Simulating a database error to trigger compensation!");
    }
}
