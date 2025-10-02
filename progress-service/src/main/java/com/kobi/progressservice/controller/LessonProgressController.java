package com.kobi.progressservice.controller;

import com.kobi.progressservice.dto.ApiResponse;
import com.kobi.progressservice.dto.request.CompleteLessonRequest;
import com.kobi.progressservice.dto.response.ProgressResponse;
import com.kobi.progressservice.exception.SuccessCode;
import com.kobi.progressservice.service.LessonProgressService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LessonProgressController {
    LessonProgressService lessonProgressService;

    @PostMapping("/complete-lesson")
    ApiResponse<Void> completeLesson(@RequestBody @Valid CompleteLessonRequest completeLessonRequest){
        lessonProgressService.completeLesson(completeLessonRequest);
        return ApiResponse.ok(null, SuccessCode.COMPLETE_LESSON_SUCCESS);
    }

    @GetMapping
    ApiResponse<ProgressResponse> getCourseProgress(@RequestParam String courseId){
        return ApiResponse.ok(lessonProgressService.getCourseProgress(courseId), SuccessCode.GET_COURSE_PROGRESS_SUCCESS);
    }
}