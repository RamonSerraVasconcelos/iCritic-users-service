package com.iCritic.users.entrypoint.fixture;

import com.iCritic.users.entrypoint.entity.CountryResponseDto;

public class CountryResponseDtoFixture {

    public static CountryResponseDto load() {
        return CountryResponseDto.builder()
                .id(1L)
                .name("test")
                .build();
    }
}
