package com.kobi.profileservice.mapper;

import com.kobi.event.UserCreateEvent;
import com.kobi.profileservice.entity.Profile;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ProfileMapper {
    Profile toProfile(UserCreateEvent request);
}
