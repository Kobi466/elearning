package com.kobi.courseservice.repository;


import com.kobi.courseservice.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionRepository extends JpaRepository<Section, String> {
    int countByCourseId(String courseId);
}
