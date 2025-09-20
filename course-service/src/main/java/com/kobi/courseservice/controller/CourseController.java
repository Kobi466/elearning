package com.kobi.courseservice.controller;

import com.kobi.courseservice.dto.ApiResponse;
import com.kobi.courseservice.dto.request.CreatedCourseRequest;
import com.kobi.courseservice.dto.request.UpdateCourseRequest;
import com.kobi.courseservice.dto.request.UploadThumbnailRequest;
import com.kobi.courseservice.dto.response.CourseResponse;
import com.kobi.courseservice.dto.response.CreatedCourseResponse;
import com.kobi.courseservice.exception.SuccessCode;
import com.kobi.courseservice.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/v1")
public class CourseController {
    CourseService courseService;

    @PostMapping("/created")
    public ApiResponse<CreatedCourseResponse> createCourse(@Valid @RequestBody CreatedCourseRequest request) {
        return ApiResponse.ok(courseService.createCourse(request), SuccessCode.CREATED_COURSE);
    }

    @PatchMapping("/{courseId}/thumbnail")
    ApiResponse<CourseResponse> uploadThumbnail(@PathVariable String courseId,
                                                @Valid @RequestBody UploadThumbnailRequest request) {
        return ApiResponse.ok(courseService.uploadThumbnail(courseId, request), SuccessCode.UPDATED_COURSE);
    }

    @PutMapping("/{courseId}/update")
    ApiResponse<CourseResponse> updateCourse(@PathVariable String courseId,
                                             @Valid @RequestBody UpdateCourseRequest request) {
        return ApiResponse.ok(courseService.updateCourse(courseId, request), SuccessCode.UPDATED_COURSE);
    }

    @PatchMapping("/{courseId}/publish")
    ApiResponse<CourseResponse> publishCourse(@PathVariable String courseId) {
        return ApiResponse.ok(courseService.publishCourse(courseId), SuccessCode.UPDATED_COURSE);
    }

    @DeleteMapping("/{courseId}/delete")
    ApiResponse<Void> deleteCourse(@PathVariable String courseId) {
        return ApiResponse.ok(courseService.deleteCourse(courseId), SuccessCode.DELETED_COURSE);
    }
}
