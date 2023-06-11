package com.iCritic.iCritic.entrypoint.fixture;

import com.iCritic.iCritic.entrypoint.model.CountryResponseDto;

public class CountryResponseDtoFixture {

    public static CountryResponseDto load() {
        return CountryResponseDto.builder()
                .id(1L)
                .name("test")
                .build();
    }
}
