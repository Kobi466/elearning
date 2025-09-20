package com.kobi.courseservice.mapper;

import com.kobi.courseservice.dto.request.CreatedLessonRequest;
import com.kobi.courseservice.dto.response.CreatedLessonResponse;
import com.kobi.courseservice.entity.Lesson;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = Lesson.class)
public interface LessonMapper {
    CreatedLessonResponse toResponse(Lesson lesson);

    Lesson toEntity(CreatedLessonRequest request);

}
