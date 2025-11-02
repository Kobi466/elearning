package com.kobi.enrollmentservice.controller;

import com.kobi.enrollmentservice.dto.ApiResponse;
import com.kobi.enrollmentservice.dto.request.InternalEnrollmentRequest;
import com.kobi.enrollmentservice.exception.SuccessCode;
import com.kobi.enrollmentservice.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalController {
    EnrollmentService enrollmentService;

    @PostMapping("/enrollment")
    public ApiResponse<Void> enrollment(@RequestBody @Valid InternalEnrollmentRequest enrollmentRequest) {
//        enrollmentService.internalEnrollmentNoFree(enrollmentRequest);
        return ApiResponse.ok(null,( SuccessCode.ENROLLMENT_SUCCESS));
    }
}
