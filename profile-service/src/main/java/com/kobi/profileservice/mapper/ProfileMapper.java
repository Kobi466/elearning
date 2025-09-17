package com.kobi.profileservice.mapper;

import com.kobi.profileservice.dto.repsonse.ProfileResponse;
import com.kobi.profileservice.dto.request.ProfileUpdateRequest;
import com.kobi.profileservice.entity.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface ProfileMapper {

    ProfileResponse toProfileResponse(Profile profile);

    void update(@MappingTarget Profile profile, ProfileUpdateRequest request);
}
