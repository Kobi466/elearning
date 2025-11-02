package com.kobi.enrollmentservice.mapper;

import com.kobi.enrollmentservice.dto.response.EnrollmentResponse;
import com.kobi.enrollmentservice.entity.Enrollment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = Enrollment.class)
public interface EnrollmentMapper {
    EnrollmentResponse toResponse(Enrollment enrollment);
}
