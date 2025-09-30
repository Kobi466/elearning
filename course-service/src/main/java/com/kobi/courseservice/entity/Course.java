package com.kobi.courseservice.entity;

import com.kobi.courseservice.entity.enums.CourseStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "courses")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String title;
    String description;
    String thumbnailUrl;
    BigDecimal price;
    String userId;
    @Enumerated(EnumType.STRING)
    CourseStatus status;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    List<Section> sections;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        this.status = CourseStatus.DRAFT;
    }
}

