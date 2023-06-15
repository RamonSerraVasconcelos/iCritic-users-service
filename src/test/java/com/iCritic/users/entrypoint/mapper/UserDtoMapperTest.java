package com.iCritic.users.entrypoint.mapper;

import com.iCritic.users.core.fixture.UserFixture;
import com.iCritic.users.core.model.User;
import com.iCritic.users.entrypoint.fixture.UserRequestDtoFixture;
import com.iCritic.users.entrypoint.model.UserRequestDto;
import com.iCritic.users.entrypoint.model.UserResponseDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserDtoMapperTest {

    @Test
    void givenUserRequestDto_convertUserToUser_thenReturnUser() {
        UserRequestDto userRequestDto = UserRequestDtoFixture.load();

        User user = UserDtoMapper.INSTANCE.userRequestDtoToUser(userRequestDto);

        assertNotNull(user);
        assertEquals(user.getName(), userRequestDto.getName());
        assertEquals(user.getEmail(), userRequestDto.getEmail());
        assertEquals(user.getPassword(), userRequestDto.getPassword());
        assertEquals(user.getDescription(), userRequestDto.getDescription());
        assertEquals(user.getRole().toString(), userRequestDto.getRole());
        assertEquals(user.getCountryId(), userRequestDto.getCountryId());
    }

    @Test
    void givenUser_convertUserToUserResponseDto_thenReturnUserResponseDto() {
        User user = UserFixture.load();

        UserResponseDto userResponseDto = UserDtoMapper.INSTANCE.userToUserResponseDto(user);

        assertNotNull(userResponseDto);
        assertEquals(userResponseDto.getId(), user.getId());
        assertEquals(userResponseDto.getName(), user.getName());
        assertEquals(userResponseDto.getEmail(), user.getEmail());
        assertEquals(userResponseDto.getDescription(), user.getDescription());
        assertEquals(userResponseDto.isActive(), user.isActive());
        assertEquals(userResponseDto.getRole(), user.getRole());
        assertEquals(userResponseDto.getCountry().getId(), user.getCountry().getId());
        assertEquals(userResponseDto.getCountry().getName(), user.getCountry().getName());
    }
}
