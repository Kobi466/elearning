package com.kobi.paymentservice.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnrollmentResponse {
    String id;
    String userId;
    String courseId;
    LocalDateTime enrollmentDate;

}
