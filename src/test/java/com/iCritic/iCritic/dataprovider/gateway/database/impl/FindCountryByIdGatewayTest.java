package com.iCritic.iCritic.dataprovider.gateway.database.impl;

import com.iCritic.iCritic.core.fixture.CountryFixture;
import com.iCritic.iCritic.core.model.Country;
import com.iCritic.iCritic.dataprovider.gateway.database.entity.CountryEntity;
import com.iCritic.iCritic.dataprovider.gateway.database.fixture.CountryEntityFixture;
import com.iCritic.iCritic.dataprovider.gateway.database.repository.CountryRepository;
import com.iCritic.iCritic.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindCountryByIdGatewayTest {

    @InjectMocks
    private FindCountryByIdGateway findCountryByIdGateway;

    @Mock
    private CountryRepository countryRepository;

    @Test
    void givenExecution_callCountryRepository_thenReturnCountry() {
        Country country = CountryFixture.load();
        CountryEntity countryEntity = CountryEntityFixture.load();

        when(countryRepository.findById(any())).thenReturn(Optional.of(countryEntity));

        Country foundCountry = findCountryByIdGateway.execute(country.getId());

        verify(countryRepository).findById(any());
        assertNotNull(foundCountry);
        assertEquals(foundCountry.getId(), country.getId());
        assertEquals(foundCountry.getName(), country.getName());
    }

    @Test
    void givenExecution_whenCountryIsNotFound_thenThrowResourceNotFoundException() {
        Country country = CountryFixture.load();

        when(countryRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> findCountryByIdGateway.execute(country.getId()));

        verify(countryRepository).findById(any());
    }
}
