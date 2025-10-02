package com.kobi.profileservice.controller;

import com.kobi.profileservice.dto.ApiResponse;
import com.kobi.profileservice.dto.repsonse.ProfileResponse;
import com.kobi.profileservice.exception.SuccessCode;
import com.kobi.profileservice.service.ProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalController {
    ProfileService profileService;
    @PostMapping("/get-by-ids")
    ApiResponse<List<ProfileResponse>> getProfileByIds(@RequestBody List<String> userIds) {
        return ApiResponse.ok(profileService.getProfilesByIds(userIds), SuccessCode.GET_PROFILE_SUCCESS);
    }
}
