package com.kobi.progressservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompleteLessonRequest {
    @NotBlank
    String courseId;
    @NotBlank
    String sectionId;
    @NotBlank
    String lessonId;
}
