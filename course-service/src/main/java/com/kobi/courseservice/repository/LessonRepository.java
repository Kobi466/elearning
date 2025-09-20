package com.kobi.courseservice.repository;

import com.kobi.courseservice.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, String> {
    int countBySectionId(String sectionId);
}
