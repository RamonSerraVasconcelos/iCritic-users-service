package com.iCritic.users.core.usecase;

import com.iCritic.users.core.fixture.CountryFixture;
import com.iCritic.users.core.usecase.boundary.FindAllCountriesBoundary;
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
class FindAllCountriesUseCaseTest {

    @InjectMocks
    private FindAllCountriesUseCase findAllCountriesUseCase;

    @Mock
    private FindAllCountriesBoundary findAllCountriesBoundary;

    @Test
    void givenExecution_thenReturnCountries() {
        var countries = List.of(CountryFixture.load(), CountryFixture.load());

        when(findAllCountriesBoundary.execute()).thenReturn(countries);

        var returnedCountries = findAllCountriesUseCase.execute();

        verify(findAllCountriesBoundary).execute();
        assertThat(returnedCountries).isNotNull().isNotEmpty().usingRecursiveComparison().isEqualTo(countries);
    }
}