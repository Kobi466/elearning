package com.kobi.profileservice.service;

import com.kobi.event.UserCreateEvent;
import com.kobi.profileservice.exception.AppException;
import com.kobi.profileservice.exception.ErrorCode;
import com.kobi.profileservice.mapper.ProfileMapper;
import com.kobi.profileservice.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ProfileService {
    ProfileMapper profileMapper;
    ProfileRepository profileRepository;

    @Transactional
    public void createProfile(UserCreateEvent event){
        // Best Practice: Idempotency Check.
        if(profileRepository.findByUserId(event.getUserId()).isPresent()) {
            log.warn("Profile for userId: {} already exists. Skipping creation.", event.getUserId());
            return;
        }

        log.info("Creating a new profile for userId: {}", event.getUserId());
        profileRepository.save(profileMapper.toProfile(event));
    }
}
