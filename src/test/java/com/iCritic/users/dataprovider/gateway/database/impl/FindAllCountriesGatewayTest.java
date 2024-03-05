package com.iCritic.users.dataprovider.gateway.database.impl;

import com.iCritic.users.dataprovider.gateway.database.entity.CountryEntity;
import com.iCritic.users.dataprovider.gateway.database.repository.CountryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindAllCountriesGatewayTest {

    @InjectMocks
    private FindAllCountriesGateway findAllCountriesGateway;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private Pageable pageable;

    @Mock
    private Page<CountryEntity> pageableCountry;

    @Test
    void givenExecution_thenReturnCountries() {
        when(countryRepository.findAllByOrderByIdAsc(pageable)).thenReturn(pageableCountry);

        var returnedCountries = findAllCountriesGateway.execute(pageable);

        verify(countryRepository).findAllByOrderByIdAsc(pageable);
        assertThat(returnedCountries).isNotNull();
    }
}