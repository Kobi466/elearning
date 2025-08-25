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

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ProfileService {
    ProfileMapper profileMapper;
    ProfileRepository profileRepository;
    public void createProfile(UserCreateEvent request){
        profileRepository.save(profileMapper.toProfile(request));
    }
}
