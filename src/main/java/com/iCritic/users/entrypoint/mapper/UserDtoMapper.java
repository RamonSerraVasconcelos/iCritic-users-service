package com.iCritic.users.entrypoint.mapper;

import com.iCritic.users.entrypoint.model.UserRequestDto;
import com.iCritic.users.core.model.User;
import com.iCritic.users.entrypoint.model.UserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class UserDtoMapper {
    public static final UserDtoMapper INSTANCE = Mappers.getMapper(UserDtoMapper.class);

    @Mapping(target = "country", source = "country")
    public abstract UserResponseDto userToUserResponseDto(User user);

    public abstract User userRequestDtoToUser(UserRequestDto userRequestDto);
}
