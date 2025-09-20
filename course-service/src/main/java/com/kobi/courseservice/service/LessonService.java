package com.kobi.courseservice.service;

import com.kobi.courseservice.dto.request.CreatedLessonRequest;
import com.kobi.courseservice.dto.request.UpdateLessonRequest;
import com.kobi.courseservice.dto.response.CreatedLessonResponse;
import com.kobi.courseservice.entity.Lesson;
import com.kobi.courseservice.exception.AppException;
import com.kobi.courseservice.exception.ErrorCode;
import com.kobi.courseservice.mapper.LessonMapper;
import com.kobi.courseservice.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class LessonService {
    LessonMapper lessonMapper;
    LessonRepository lessonRepository;
    SectionService sectionService;

    @PreAuthorize("hasRole('ADMIN')")
    public CreatedLessonResponse createdLesson(String sectionId, CreatedLessonRequest request) {
        var section = sectionService.getSectionById(sectionId);
        var lesson = lessonMapper.toEntity(request);
        lesson.setSection(section);
        var orderIndex = lessonRepository.countBySectionId(sectionId);
        lesson.setOrderIndex(orderIndex);
        return lessonMapper.toResponse(lessonRepository.save(lesson));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Void deleteLesson(String lessonId) {
        lessonRepository.delete(this.getLessonById(lessonId));
        return null;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public CreatedLessonResponse updateLesson(String lessonId, UpdateLessonRequest request) {
        var lesson = this.getLessonById(lessonId);
        lesson.setTitle(request.getTitle());
        return lessonMapper.toResponse(lessonRepository.save(lesson));
    }

    @PreAuthorize("hasRole('ADMIN')")
    private Lesson getLessonById(String lessonId) {
        var lesson = lessonRepository.findById(lessonId).orElseThrow(
                () -> new AppException(ErrorCode.LESSON_NOT_FOUND)
        );
        var userId = lesson.getSection().getCourse().getUserId();
        if (!userId.equals(this.userId()))
            throw new AppException(ErrorCode.AUTHENTICATION_FAILED);
        return lesson;
    }

    private String userId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
