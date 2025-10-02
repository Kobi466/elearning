package com.kobi.reviewservice.repository;

import com.kobi.reviewservice.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {
    Page<Review> findByCourseId(String courseId, Pageable pageable);
    boolean existsByUserIdAndCourseId(String userId, String courseId);
}
