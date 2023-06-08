package com.iCritic.iCritic.entrypoint.fixture;

import com.iCritic.iCritic.core.enums.Role;
import com.iCritic.iCritic.entrypoint.model.UserResponseDto;

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
