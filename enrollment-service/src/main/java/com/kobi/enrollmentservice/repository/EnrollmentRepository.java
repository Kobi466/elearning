package com.kobi.enrollmentservice.repository;

import com.kobi.enrollmentservice.entity.Enrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, String> {
    boolean existsByUserIdAndCourseId(String userId, String courseId);
    Page<Enrollment> findByUserId(String userId, Pageable pageable);
}
