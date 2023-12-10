package com.iCritic.users.entrypoint.mapper;

import com.iCritic.users.core.fixture.CountryFixture;
import com.iCritic.users.core.model.Country;
import com.iCritic.users.entrypoint.entity.CountryResponseDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CountryDtoMapperTest {

    @Test
    void givenCountry_thenReturnCountryResponseDto() {
        Country country = CountryFixture.load();

        CountryResponseDto countryResponseDto = CountryDtoMapper.INSTANCE.countryToCountryResponseDto(country);

        assertThat(countryResponseDto).isNotNull().usingRecursiveComparison().isEqualTo(country);
    }
}