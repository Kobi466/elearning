package com.kobi.reviewservice.entity;

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
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String userId;
    String courseId;
    int rating;
    String comment;
    LocalDateTime createdAt;
    @PrePersist
    void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
