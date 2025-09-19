package com.kobi.profileservice.service;


import com.kobi.avro.UserPayload;
import com.kobi.profileservice.dto.repsonse.ProfileResponse;
import com.kobi.profileservice.dto.request.ProfileUpdateRequest;
import com.kobi.profileservice.entity.Profile;
import com.kobi.profileservice.exception.AppException;
import com.kobi.profileservice.exception.ErrorCode;
import com.kobi.profileservice.mapper.ProfileMapper;
import com.kobi.profileservice.repository.ProfileRepository;
import com.kobi.profileservice.repository.httpClient.FileClient;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ProfileService {
    ProfileRepository profileRepository;
    ProfileMapper profileMapper;
    FileClient fileClient;

    // *** SỬA LỖI 1: Xử lý an toàn các trường null khi tạo profile từ Kafka event ***
    public void createProfileFromPayload(UserPayload payload){
        Profile profile = Profile.builder()
                .userId(payload.getUserId())
                .userName(payload.getUserName())
                .email(payload.getEmail())
                // Nếu fullName null thì dùng userName thay thế
                .fullName(Objects.toString(payload.getFullName(), payload.getUserName())) 
                .firstName(Objects.toString(payload.getFirstName(), ""))
                .lastName(Objects.toString(payload.getLastName(), ""))
                .locale(Objects.toString(payload.getLocale(), "en"))
                .build();
        profileRepository.save(profile);
        log.info("PROFILE CREATED for user ID: {}", payload.getUserId());
    }

    private Profile getProfileByUserId(String userId) {
        return profileRepository.findByUserId(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    public ProfileResponse getMyProfile() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        var profile = getProfileByUserId(userId);
        return profileMapper.toProfileResponse(profile);
    }

    public ProfileResponse updateProfile(ProfileUpdateRequest request) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        var profile = getProfileByUserId(userId);
        profileMapper.update(profile, request);
        return profileMapper.toProfileResponse(profileRepository.save(profile));
    }

    public ProfileResponse updateAvatar(MultipartFile avatar) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        var profile = getProfileByUserId(userId);
        var urlAvatar = fileClient.uploadFile(avatar).block().getData().getUrl();
        profile.setAvatar(urlAvatar);
        return profileMapper.toProfileResponse(profileRepository.save(profile));
    }

    @PreAuthorize( "hasRole('ADMIN')")
    public void deleteAll () {
        profileRepository.deleteAll();
    }

    @PreAuthorize( "hasRole('ADMIN')")
    public List<ProfileResponse> getAll(){
        return profileRepository.findAll().stream().map(profileMapper::toProfileResponse).toList();
    }

}
