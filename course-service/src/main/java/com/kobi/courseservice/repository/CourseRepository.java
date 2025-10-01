package com.kobi.courseservice.repository;

import com.kobi.courseservice.entity.Course;
import com.kobi.courseservice.entity.enums.CourseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    Optional<Course> findCourseDetailsById(String courseId);
    Page<Course> findCourseByStatus(CourseStatus status, Pageable pageable);
    Page<Course> getCourseByUserId(String userId, Pageable pageable);
    Page<Course> findCourseByUserIdAndStatus(String userId, CourseStatus status, Pageable pageable);
}

