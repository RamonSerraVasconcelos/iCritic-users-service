package com.iCritic.iCritic.entrypoint.fixture;

import com.iCritic.iCritic.entrypoint.model.UserRequestDto;

public class UserRequestDtoFixture {

    public static UserRequestDto load() {
        return UserRequestDto.builder()
                .name("John Doe")
                .email("test@test.test")
                .password("test")
                .description("test description")
                .role("DEFAULT")
                .countryId(1L)
                .build();
    }
}
