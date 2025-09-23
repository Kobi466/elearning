package com.kobi.courseservice.repository;

import com.kobi.courseservice.entity.Course;
import com.kobi.courseservice.entity.enums.CourseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
/* <<<<<<<<<<<<<<  ✨ Windsurf Command ⭐ >>>>>>>>>>>>>>>> */
    /**
     * Finds courses by status and pageable.
     * @param status the course status.
     * @param pageable the pageable.
     * @return the page of courses.
     */
/* <<<<<<<<<<  2bec16ea-6d71-4ff4-8c15-b47fefb3704e  >>>>>>>>>>> */
    Page<Course> findCourseByStatus(CourseStatus status, Pageable pageable);
}
