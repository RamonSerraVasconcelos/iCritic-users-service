package com.iCritic.iCritic.core.user.mapper;

import com.iCritic.iCritic.core.user.dto.UserRequestDto;
import com.iCritic.iCritic.core.user.User;
import com.iCritic.iCritic.core.user.dto.UserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class UserMapper {
    public static final UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "country", source = "country")
    public abstract UserResponseDto userToUserResponseDto(User user);

    public abstract User userRequestDtoToUser(UserRequestDto userRequestDto);
}
