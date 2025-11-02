package com.kobi.enrollmentservice.controller;

import com.kobi.enrollmentservice.dto.ApiResponse;
import com.kobi.enrollmentservice.dto.PageResponse;
import com.kobi.enrollmentservice.dto.request.EnrollmentRequest;
import com.kobi.enrollmentservice.dto.response.EnrollmentResponse;
import com.kobi.enrollmentservice.dto.response.EnrollmentStatusResponse;
import com.kobi.enrollmentservice.dto.response.MyEnrollmentResponse;
import com.kobi.enrollmentservice.exception.SuccessCode;
import com.kobi.enrollmentservice.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EnrollmentController {
    EnrollmentService enrollmentService;

    @PostMapping("/enrollment")
    public ApiResponse<EnrollmentResponse> enrollment(@RequestBody @Valid EnrollmentRequest enrollmentRequest) {
        return ApiResponse.ok(enrollmentService.enrollment(enrollmentRequest), SuccessCode.ENROLLMENT_SUCCESS);
    }
    @GetMapping("/my-enrollment")
    ApiResponse<PageResponse<MyEnrollmentResponse>> getMyEnrollments(Pageable pageable) {
        return ApiResponse.ok(enrollmentService.getMyEnrollments(pageable), SuccessCode.GET_MY_ENROLLMENT_SUCCESS);
    }
    @GetMapping("/status")
    ApiResponse<EnrollmentStatusResponse> getEnrollmentStatus(String courseId) {
        return ApiResponse.ok(enrollmentService.getEnrollmentStatus(courseId), SuccessCode.ENROLLMENT_SUCCESS);
    }
}
