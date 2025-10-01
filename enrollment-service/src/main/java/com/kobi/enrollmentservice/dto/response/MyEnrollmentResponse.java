package com.kobi.enrollmentservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class MyEnrollmentResponse {
    String id;
    LocalDateTime enrollmentDate;
    String courseId;
    String courseTitle;
    String courseThumbnailUrl;
}
