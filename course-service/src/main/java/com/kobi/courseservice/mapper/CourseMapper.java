package com.kobi.courseservice.mapper;

import com.kobi.courseservice.dto.request.CreatedCourseRequest;
import com.kobi.courseservice.dto.request.UpdateCourseRequest;
import com.kobi.courseservice.dto.response.CourseResponse;
import com.kobi.courseservice.dto.response.CreatedCourseResponse;
import com.kobi.courseservice.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = Course.class)
public interface CourseMapper {
    //    @Mapping(target = "imageUrl", ignore = true)
    Course toEntity(CreatedCourseRequest createdCourseRequest);

    CreatedCourseResponse toResponseCreated(Course course);

    CourseResponse toResponse(Course course);

    void updateEntity(@MappingTarget Course course, UpdateCourseRequest request);
}
