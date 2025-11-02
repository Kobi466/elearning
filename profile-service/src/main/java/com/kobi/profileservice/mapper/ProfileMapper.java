package com.kobi.profileservice.mapper;

import com.kobi.profileservice.dto.repsonse.ProfileResponse;
import com.kobi.profileservice.dto.request.ProfileUpdateRequest;
import com.kobi.profileservice.entity.Profile;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(componentModel = "spring")
public interface ProfileMapper {

    ProfileResponse toProfileResponse(Profile profile);

    // *** SỬA LỖI 2: Bỏ qua các giá trị null khi cập nhật ***
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget Profile profile, ProfileUpdateRequest request);
}
