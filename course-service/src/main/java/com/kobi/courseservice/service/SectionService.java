package com.kobi.courseservice.service;

import com.kobi.courseservice.dto.request.CreatedSectionRequest;
import com.kobi.courseservice.dto.request.UpdateSectionRequest;
import com.kobi.courseservice.dto.response.CreatedSectionResponse;
import com.kobi.courseservice.entity.Section;
import com.kobi.courseservice.exception.AppException;
import com.kobi.courseservice.exception.ErrorCode;
import com.kobi.courseservice.mapper.SectionMapper;
import com.kobi.courseservice.repository.SectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SectionService {
    SectionMapper sectionMapper;
    SectionRepository sectionRepository;
    CourseService courseService;

    @PreAuthorize("hasRole('ADMIN')")
    public CreatedSectionResponse createdSection(String courseId, CreatedSectionRequest request) {
        var course = courseService.getCourseById(courseId);
        var section = sectionMapper.toEntity(request);
        section.setCourse(course);
        var orderIndex = sectionRepository.countByCourseId(courseId);
        section.setOrderIndex(orderIndex);
        return sectionMapper.toResponse(sectionRepository.save(section));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Void deleteSection(String sectionId) {
        var section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new AppException(ErrorCode.SECTION_NOT_FOUND));
        var userId = section.getCourse().getUserId();
        if (!userId.equals(this.getUserId())) {
            throw new AppException(ErrorCode.AUTHENTICATION_FAILED);
        }
        sectionRepository.deleteById(sectionId);
        return null;
    }

    private String getUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    protected Section getSectionById(String sectionId) {
        var userId = this.getUserId();
        var section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new AppException(ErrorCode.SECTION_NOT_FOUND));
        if (!section.getCourse().getUserId().equals(userId))
            throw new AppException(ErrorCode.AUTHENTICATION_FAILED);
        return section;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public CreatedSectionResponse updateSection(String sectionId, UpdateSectionRequest request) {
        var section = this.getSectionById(sectionId);
        sectionMapper.updateEntity(section, request);
        return sectionMapper.toResponse(sectionRepository.save(section));
    }
}
