package com.kobi.enrollmentservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

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
