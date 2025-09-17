package com.kobi.profileservice.controller;

import com.kobi.profileservice.dto.ApiResponse;
import com.kobi.profileservice.dto.repsonse.ProfileResponse;
import com.kobi.profileservice.dto.request.ProfileUpdateRequest;
import com.kobi.profileservice.exception.SuccessCode;
import com.kobi.profileservice.service.ProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileController {
    ProfileService profileService;

    @PutMapping("update")
    ApiResponse<ProfileResponse> updateProfile(@RequestBody ProfileUpdateRequest request) {
        return ApiResponse.ok(profileService.updateProfile(request), SuccessCode.PROFILE_UPDATED);
    }

    @PostMapping("/upload")
    ApiResponse<ProfileResponse> updateAvatarProfile(@RequestParam("file") MultipartFile avatar) {
        return ApiResponse.ok(profileService.updateAvatar(avatar), SuccessCode.AVATAR_UPDATED);
    }

    @DeleteMapping("/delete/profiles")
    public ApiResponse<String> deleteAll() {
        profileService.deleteAll();
        return ApiResponse.ok("Delete all success", SuccessCode.DELETE_ALL_SUCCESS);
    }

    @GetMapping("/getAll")
    public ApiResponse<List<ProfileResponse>> getAll() {
        return ApiResponse.ok(profileService.getAll(), SuccessCode.GET_PROFILE_SUCCESS);
    }
}
