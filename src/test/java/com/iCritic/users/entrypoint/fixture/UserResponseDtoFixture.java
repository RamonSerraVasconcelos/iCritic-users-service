package com.iCritic.users.entrypoint.fixture;

import com.iCritic.users.core.enums.Role;
import com.iCritic.users.entrypoint.model.UserResponseDto;

import java.time.LocalDateTime;

public class UserResponseDtoFixture {

    public static UserResponseDto load() {
        return UserResponseDto.builder()
                .id(1L)
                .name("name")
                .email("email")
                .description("description")
                .active(true)
                .role(Role.DEFAULT)
                .country(CountryResponseDtoFixture.load())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
