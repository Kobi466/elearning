package com.kobi.progressservice.repository;

import com.kobi.progressservice.entity.LessonProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonProgressRepository extends JpaRepository<LessonProgress,String> {
    boolean existsByUserIdAndLessonId(String userId, String lessonId);
    List<LessonProgress> findByUserIdAndCourseId(String userId, String courseId);
}
