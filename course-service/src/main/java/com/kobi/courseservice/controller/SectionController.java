package com.kobi.courseservice.controller;

import com.kobi.courseservice.dto.ApiResponse;
import com.kobi.courseservice.dto.request.CreatedSectionRequest;
import com.kobi.courseservice.dto.request.UpdateSectionRequest;
import com.kobi.courseservice.dto.response.CreatedSectionResponse;
import com.kobi.courseservice.exception.SuccessCode;
import com.kobi.courseservice.service.SectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/section")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class SectionController {
    SectionService sectionService;

    @PostMapping("/created/{courseId}")
    ApiResponse<CreatedSectionResponse> createSection(@PathVariable String courseId,
                                                      @Valid @RequestBody CreatedSectionRequest request) {
        return ApiResponse.ok(sectionService.createdSection(courseId, request), SuccessCode.CREATED_SECTION);
    }

    @PutMapping("/update/{sectionId}")
    ApiResponse<CreatedSectionResponse> updateSection(@PathVariable String sectionId,
                                                      @Valid @RequestBody UpdateSectionRequest request) {
        return ApiResponse.ok(sectionService.updateSection(sectionId, request), SuccessCode.UPDATED_SECTION);
    }

    @DeleteMapping("/{sectionId}/delete")
    ApiResponse<Void> deleteSection(@PathVariable String sectionId) {
        return ApiResponse.ok(sectionService.deleteSection(sectionId), SuccessCode.DELETED_SECTION);
    }

}
