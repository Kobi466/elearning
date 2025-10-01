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

    public CourseResponse getCourse(String courseId) {
        var course = courseRepository.findCourseDetailsById(courseId);
        if (course.isEmpty()||course.get().getStatus()==CourseStatus.DRAFT)
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);
        return courseMapper.toResponse(course.get());
    }

    public List<CourseResponse> getCourseByIds(List<String> courseIds) {
        List<Course> courses = courseRepository.findAllById(courseIds);
        return courses.stream()
                .map(courseMapper::toResponse)
                .collect(Collectors.toList());
    }

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

    public PageResponse<CourseResponse> getCourseWithStatusByUserId(String userId, Pageable pageable){
        Page<Course> coursePage = courseRepository.findCourseByUserIdAndStatus(userId, CourseStatus.PUBLISHED, pageable);
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
        course.setUpdatedAt(LocalDateTime.now());
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
        course.setUpdatedAt(LocalDateTime.now());
        return courseMapper.toResponse(courseRepository.save(course));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Void deleteCourse(String courseId) {
        var course = this.getCourseById(courseId);
        courseRepository.delete(course);
        return null;
    }
}

