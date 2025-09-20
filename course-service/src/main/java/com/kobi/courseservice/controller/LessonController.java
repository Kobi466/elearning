package com.kobi.courseservice.controller;

import com.kobi.courseservice.dto.ApiResponse;
import com.kobi.courseservice.dto.request.CreatedLessonRequest;
import com.kobi.courseservice.dto.request.UpdateLessonRequest;
import com.kobi.courseservice.dto.response.CreatedLessonResponse;
import com.kobi.courseservice.exception.SuccessCode;
import com.kobi.courseservice.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/lesson")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class LessonController {
    LessonService lessonService;

    @PostMapping("/create/{sectionId}")
    ApiResponse<CreatedLessonResponse> createdLesson(@PathVariable String sectionId,
                                                     @Valid @RequestBody CreatedLessonRequest request) {
        return ApiResponse.ok(lessonService.createdLesson(sectionId, request), SuccessCode.CREATED_LESSON);
    }

    @DeleteMapping("/{lessonId}/delete")
    ApiResponse<Void> deleteLesson(@PathVariable String lessonId) {
        return ApiResponse.ok(lessonService.deleteLesson(lessonId), SuccessCode.DELETED_LESSON);
    }

    @PutMapping("/update/{lessonId}")
    ApiResponse<CreatedLessonResponse> updateLesson(@PathVariable String lessonId,
                                                    @Valid @RequestBody UpdateLessonRequest request) {
        return ApiResponse.ok(lessonService.updateLesson(lessonId, request), SuccessCode.UPDATED_LESSON);
    }
}
