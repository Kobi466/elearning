package com.kobi.courseservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CourseResponse {
    String id;
    String title;
    String description;
    BigDecimal price;
    String thumbnailUrl;
    String userId;
    String status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
