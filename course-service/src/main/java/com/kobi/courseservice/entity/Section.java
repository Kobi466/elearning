package com.kobi.courseservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "sections")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    Course course;
    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL)
    List<Lesson> lessons;
    @Column(nullable = false, length = 255)
    String title;
    @Column(columnDefinition = "int default 0", nullable = false)
    int orderIndex;
}
