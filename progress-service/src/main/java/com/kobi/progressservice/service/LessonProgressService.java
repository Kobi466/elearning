package com.kobi.progressservice.service;

import com.kobi.progressservice.dto.request.CompleteLessonRequest;
import com.kobi.progressservice.dto.response.ProgressResponse;
import com.kobi.progressservice.entity.LessonProgress;
import com.kobi.progressservice.exception.AppException;
import com.kobi.progressservice.exception.ErrorCode;
import com.kobi.progressservice.repository.LessonProgressRepository;
import com.kobi.progressservice.repository.httpClient.EnrollmentClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LessonProgressService {
    LessonProgressRepository lessonProgressRepository;
    EnrollmentClient enrollmentClient;
    public void completeLesson(CompleteLessonRequest completeLessonRequest) {
        if(!Objects.requireNonNull(enrollmentClient
                .getEnrollmentStatus(completeLessonRequest.getCourseId()).block())
                .getData().isEnrolled()
        )
            throw new AppException(ErrorCode.REGISTERED);
        if(lessonProgressRepository.existsByUserIdAndLessonId(getUserId(), completeLessonRequest.getLessonId()))
            return;
        lessonProgressRepository.save(LessonProgress.builder()
                .userId(getUserId())
                .courseId(completeLessonRequest.getCourseId())
                .sectionId(completeLessonRequest.getSectionId())
                .lessonId(completeLessonRequest.getLessonId())
                .build());
    }

    public ProgressResponse getCourseProgress(String courseId){
        List<LessonProgress> lessonProgresses = lessonProgressRepository
                .findByUserIdAndCourseId(getUserId(), courseId);
        List<String> completedLessonIds = lessonProgresses.stream()
                .map(LessonProgress::getLessonId).toList();
        return ProgressResponse.builder()
                .completedLessonIds(completedLessonIds)
                .build();
    }
    private String getUserId(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
