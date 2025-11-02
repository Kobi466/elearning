package com.kobi.progressservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "lesson_progress")
public class LessonProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String userId;
    String courseId;
    String sectionId;
    String lessonId;
    LocalDateTime completeAt;
    @PrePersist
    void prePersist() {
        completeAt = LocalDateTime.now();
    }
}
