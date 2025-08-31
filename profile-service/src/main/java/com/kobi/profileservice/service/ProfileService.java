package com.kobi.profileservice.service;


import com.kobi.avro.UserPayload;
import com.kobi.profileservice.entity.Profile;
import com.kobi.profileservice.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ProfileService {
    ProfileRepository profileRepository;

    public void createProfileFromPayload(UserPayload payload){
        profileRepository.save(Profile.builder()
                .userId(payload.getUserId())
                .userName(payload.getUserName())
                .email(payload.getEmail())
                .fullName(payload.getFullName())
                .firstName(payload.getFirstName())
                .lastName(payload.getLastName())
                .locale(payload.getLocale())
                .build());
        log.info("PROFILE CREATED");
    }
}
