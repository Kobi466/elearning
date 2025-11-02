package com.kobi.enrollmentservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String userId;
    String courseId;
    LocalDateTime enrollmentDate;

    @PrePersist
    void onCreate(){
        this.enrollmentDate = LocalDateTime.now();
    }
}
