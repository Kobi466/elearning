package com.kobi.courseservice.mapper;

import com.kobi.courseservice.dto.request.CreatedSectionRequest;
import com.kobi.courseservice.dto.request.UpdateSectionRequest;
import com.kobi.courseservice.dto.response.CreatedSectionResponse;
import com.kobi.courseservice.entity.Section;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = Section.class)
public interface SectionMapper {
    Section toEntity(CreatedSectionRequest createdSectionRequest);

    CreatedSectionResponse toResponse(Section section);

    void updateEntity(@MappingTarget Section section, UpdateSectionRequest request);
}

