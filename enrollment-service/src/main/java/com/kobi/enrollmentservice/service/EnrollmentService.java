package com.kobi.enrollmentservice.service;

import com.kobi.enrollmentservice.dto.PageResponse;
import com.kobi.enrollmentservice.dto.request.EnrollmentRequest;
import com.kobi.enrollmentservice.dto.response.CourseResponse;
import com.kobi.enrollmentservice.dto.response.EnrollmentResponse;
import com.kobi.enrollmentservice.dto.response.EnrollmentStatusResponse;
import com.kobi.enrollmentservice.dto.response.MyEnrollmentResponse;
import com.kobi.enrollmentservice.entity.Enrollment;
import com.kobi.enrollmentservice.exception.AppException;
import com.kobi.enrollmentservice.exception.ErrorCode;
import com.kobi.enrollmentservice.mapper.EnrollmentMapper;
import com.kobi.enrollmentservice.repository.EnrollmentRepository;
import com.kobi.enrollmentservice.repository.httpClient.CourseClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EnrollmentService {
    EnrollmentRepository enrollmentRepository;
    EnrollmentMapper enrollmentMapper;
    CourseClient courseClient;

    public EnrollmentResponse enrollment(EnrollmentRequest enrollmentRequest) {
        var userId = getUserId();
        var courseResponse = courseClient.getCourseById(enrollmentRequest.getCourseId());
        var courseId = courseResponse.block().getData().getId();
        if(courseResponse.block().getData().getPrice()!=null&&courseResponse.block().getData().getPrice().compareTo(BigDecimal.ZERO)>0) {
            throw new AppException(ErrorCode.COURSE_NO_FREE);
        }
        if (enrollmentRepository.existsByUserIdAndCourseId(userId, courseId)) {
            throw new AppException(ErrorCode.REGISTERED);
        }
        var enrollment = enrollmentRepository.save(Enrollment
                .builder()
                        .enrollmentDate(LocalDateTime.now())
                        .userId(userId)
                        .courseId(courseId)
                .build()
        );
        return enrollmentMapper.toResponse(enrollment);
    }

    public PageResponse<MyEnrollmentResponse> getMyEnrollments(Pageable pageable) {
        var userId = getUserId();
        Page<Enrollment> enrollmentPage = enrollmentRepository.findByUserId(userId, pageable);
        if (enrollmentPage.isEmpty()) {
            return new PageResponse<>(Collections.emptyList(), 0,0,0,0,true);
        }
        List<String> courseIds = enrollmentPage.getContent()
                .stream().map(Enrollment::getCourseId)
                .collect(Collectors.toList());
        List<CourseResponse> courses = courseClient.getCourseByIds(courseIds)
                .block().getData();
        Map<String, CourseResponse> courseMap = courses.stream()
                .collect(Collectors.toMap(CourseResponse::getId, course -> course));
        List<MyEnrollmentResponse> responseContent = enrollmentPage.getContent()
                .stream().map(enrollment -> {
                    CourseResponse course = courseMap.get(enrollment.getCourseId());
                    return MyEnrollmentResponse.builder()
                            .id(enrollment.getId())
                            .enrollmentDate(enrollment.getEnrollmentDate())
                            .courseId(course.getId())
                            .courseTitle(course.getTitle())
                            .courseThumbnailUrl(course.getThumbnailUrl())
                            .build();
                }).toList();
        return PageResponse.<MyEnrollmentResponse>builder()
                .content(responseContent)
                .pageNo(enrollmentPage.getNumber())
                .pageSize(enrollmentPage.getSize())
                .totalElement(enrollmentPage.getTotalElements())
                .totalPages(enrollmentPage.getTotalPages())
                .last(enrollmentPage.isLast())
                .build();
    }

    public EnrollmentStatusResponse getEnrollmentStatus(String courseId) {
        return EnrollmentStatusResponse.builder()
                .isEnrolled(enrollmentRepository.existsByUserIdAndCourseId(getUserId(),courseId))
                .build();
    }
    private String getUserId(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
