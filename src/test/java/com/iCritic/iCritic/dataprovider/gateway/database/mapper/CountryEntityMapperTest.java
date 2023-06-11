package com.iCritic.iCritic.dataprovider.gateway.database.mapper;

import com.iCritic.iCritic.core.model.Country;
import com.iCritic.iCritic.dataprovider.gateway.database.entity.CountryEntity;
import com.iCritic.iCritic.dataprovider.gateway.database.fixture.CountryEntityFixture;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CountryEntityMapperTest {

    @Test
    void givenCountryEntity_convertCountryEntityToCountry_thenReturnCountry() {
        CountryEntity countryEntity = CountryEntityFixture.load();

        Country country = CountryEntityMapper.INSTANCE.countryEntityToCountry(countryEntity);

        assertNotNull(country);
        assertEquals(country.getId(), countryEntity.getId());
        assertEquals(country.getName(), countryEntity.getName());
    }
}
