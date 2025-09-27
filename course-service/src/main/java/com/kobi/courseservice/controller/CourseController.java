package com.kobi.courseservice.controller;

import com.kobi.courseservice.dto.ApiResponse;
import com.kobi.courseservice.dto.PageResponse;
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
import org.hibernate.query.NativeQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/v1")
public class CourseController {
    CourseService courseService;
    @GetMapping("/getCourse")
    ApiResponse<PageResponse<CourseResponse>> getCourseWithStatusByUser(@RequestParam String userId, Pageable pageable){
        return ApiResponse.ok(courseService.getCourseWithStatusByUserId(userId, pageable), SuccessCode.GET_MY_COURSE_SUCCESS);
    }
/* <<<<<<<<<<<<<<  ✨ Windsurf Command ⭐ >>>>>>>>>>>>>>>> */
    /**
     * Returns a page of courses with status PUBLISHED.
     * @param pageable the pageable.
     * @return a page of courses.
     */
/* <<<<<<<<<<  85f9653f-b504-4835-a051-12681c2b8432  >>>>>>>>>>> */
    @GetMapping
    ApiResponse<PageResponse<CourseResponse>> getAllCoursesByPublish(Pageable pageable){
        return ApiResponse.ok( courseService.getAllCourseStatusPublish(pageable), SuccessCode.GET_COURSE_PUBLISH_SUCCESS);
    }

/* <<<<<<<<<<<<<<  ✨ Windsurf Command ⭐ >>>>>>>>>>>>>>>> */
    /**
     * Retrieves a paginated list of courses belonging to the authenticated user.
     * The method ensures that only users with the role 'ADMIN' can access this functionality.
     *
     * @param pageable the pagination information including page number, size, and sorting
     * @return a paginated response containing the courses of the authenticated user
     */
/* <<<<<<<<<<  e9646c9d-6395-489c-bbec-1cde98e6d706  >>>>>>>>>>> */
    @GetMapping("/my")
    ApiResponse<PageResponse<CourseResponse>> getCourseMy(Pageable pageable){
        return ApiResponse.ok(courseService.getCourseMy(pageable), SuccessCode.GET_MY_COURSE_SUCCESS);
    }
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
