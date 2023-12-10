package com.iCritic.users.entrypoint.resource;

import com.iCritic.users.core.fixture.CountryFixture;
import com.iCritic.users.core.model.Country;
import com.iCritic.users.core.usecase.FindAllCountriesUseCase;
import com.iCritic.users.core.usecase.FindCountryByIdUseCase;
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
class CountryResourceTest {

    @InjectMocks
    private CountryResource countryResource;

    @Mock
    private FindAllCountriesUseCase findAllCountriesUseCase;

    @Mock
    private FindCountryByIdUseCase findCountryByIdUseCase;

    @Test
    void givenCallToGetAllCountries_thenCallUseCase_andReturnCountries() {
        var countries = List.of(CountryFixture.load(), CountryFixture.load());

        when(findAllCountriesUseCase.execute()).thenReturn(countries);

        var returnedCountries = countryResource.loadAll();

        verify(findAllCountriesUseCase).execute();
        assertThat(returnedCountries).isNotNull().isNotEmpty().hasSize(2);
    }

    @Test
    void givenCallToGetCountryById_thenCallUseCase_andReturnCountry() {
        Country country = CountryFixture.load();

        when(findCountryByIdUseCase.execute(country.getId())).thenReturn(country);

        var returnedCountry = countryResource.findById(country.getId());

        verify(findCountryByIdUseCase).execute(country.getId());
        assertThat(returnedCountry.getBody()).usingRecursiveComparison().isEqualTo(country);
    }
}