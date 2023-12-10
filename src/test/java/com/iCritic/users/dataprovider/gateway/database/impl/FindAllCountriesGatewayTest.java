package com.iCritic.users.dataprovider.gateway.database.impl;

import com.iCritic.users.dataprovider.gateway.database.fixture.CountryEntityFixture;
import com.iCritic.users.dataprovider.gateway.database.repository.CountryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindAllCountriesGatewayTest {

    @InjectMocks
    private FindAllCountriesGateway findAllCountriesGateway;

    @Mock
    private CountryRepository countryRepository;

    @Test
    void givenExecution_thenReturnCountries() {
        var countriesEntities = List.of(CountryEntityFixture.load(), CountryEntityFixture.load());

        when(countryRepository.findAll()).thenReturn(countriesEntities);

        var returnedCountries = findAllCountriesGateway.execute();

        verify(countryRepository).findAll();
        assertThat(returnedCountries).isNotNull().isNotEmpty().usingRecursiveComparison().isEqualTo(countriesEntities);
    }
}