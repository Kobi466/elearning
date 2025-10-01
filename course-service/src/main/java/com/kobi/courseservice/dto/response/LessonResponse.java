package com.kobi.courseservice.dto.response;

import com.kobi.courseservice.entity.enums.ContentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class LessonResponse {
    String id;
    String title;
    String contentType;
    String contentUrl;
    int durationInSeconds;
    int orderIndex;
}
