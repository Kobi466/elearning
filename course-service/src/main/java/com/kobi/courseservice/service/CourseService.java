package com.kobi.courseservice.service;

import com.kobi.courseservice.dto.PageResponse;
import com.kobi.courseservice.dto.request.CreatedCourseRequest;
import com.kobi.courseservice.dto.request.UpdateCourseRequest;
import com.kobi.courseservice.dto.request.UploadThumbnailRequest;
import com.kobi.courseservice.dto.response.CourseResponse;
import com.kobi.courseservice.dto.response.CreatedCourseResponse;
import com.kobi.courseservice.entity.Course;
import com.kobi.courseservice.entity.enums.CourseStatus;
import com.kobi.courseservice.exception.AppException;
import com.kobi.courseservice.exception.ErrorCode;
import com.kobi.courseservice.mapper.CourseMapper;
import com.kobi.courseservice.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CourseService {
    CourseRepository courseRepository;
    CourseMapper courseMapper;

/* <<<<<<<<<<<<<<  ✨ Windsurf Command ⭐ >>>>>>>>>>>>>>>> */
    /**
     * Returns a page of courses with status PUBLISHED.
     * @param pageable the pageable.
     * @return a page of courses.
     */
/* <<<<<<<<<<  4435fa45-0780-4269-be78-555a368d295f  >>>>>>>>>>> */
    public PageResponse<CourseResponse> getAllCourseStatusPublish(Pageable pageable) {
        Page<Course> coursePage = courseRepository.findCourseByStatus(CourseStatus.PUBLISHED, pageable);

        List<CourseResponse> courseResponses = coursePage.getContent()
                .stream()
                .map(courseMapper::toResponse)
                .collect((Collectors.toList()));

        return PageResponse.<CourseResponse>builder()
                .content(courseResponses)
                .pageNo(coursePage.getNumber())
                .pageSize(coursePage.getSize())
                .totalElement(coursePage.getTotalElements())
                .totalPages(coursePage.getTotalPages())
                .last(coursePage.isLast())
                .build();
    }

    /**
     * Retrieves a paginated list of courses belonging to the authenticated user.
     * The method ensures that only users with the role 'ADMIN' can access this functionality.
     *
     * @param pageable the pagination information including page number, size, and sorting
     * @return a paginated response containing the courses of the authenticated user
     */
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<CourseResponse> getCourseMy(Pageable pageable){
        Page<Course> coursePage = courseRepository.getCourseByUserId(getUserId(), pageable);
        List<CourseResponse> courseResponses = coursePage.getContent()
                .stream()
                .map(courseMapper::toResponse)
                .collect(Collectors.toList());
        return PageResponse.<CourseResponse>builder()
                .content(courseResponses)
                .pageNo(coursePage.getNumber())
                .pageSize(coursePage.getSize())
                .totalElement(coursePage.getTotalElements())
                .totalPages(coursePage.getTotalPages())
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public CreatedCourseResponse createCourse(CreatedCourseRequest createdCourseRequest) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        var course = courseMapper.toEntity(createdCourseRequest);
        course.setUserId(userId);
        courseRepository.save(course);
        return courseMapper.toResponseCreated(course);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public CourseResponse uploadThumbnail(String courseId, UploadThumbnailRequest request) {
        var course = this.getCourseById(courseId);
        course.setThumbnailUrl(request.getThumbnailUrl());
        course.setUpdatedAt(LocalDateTime.now());
        return courseMapper.toResponse(courseRepository.save(course));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public CourseResponse updateCourse(String courseId, UpdateCourseRequest request) {
        var course = this.getCourseById(courseId);
        courseMapper.updateEntity(course, request);
        return courseMapper.toResponse(courseRepository.save(course));
    }

    private String getUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    protected Course getCourseById(String courseId) {
        var userId = this.getUserId();
        var course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        if (!course.getUserId().equals(userId))
            throw new AppException(ErrorCode.AUTHENTICATION_FAILED);
        return course;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public CourseResponse publishCourse(String courseId) {
        var course = this.getCourseById(courseId);
        course.setStatus(CourseStatus.PUBLISHED);
        return courseMapper.toResponse(courseRepository.save(course));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Void deleteCourse(String courseId) {
        var course = this.getCourseById(courseId);
        courseRepository.delete(course);
        return null;
    }
}

