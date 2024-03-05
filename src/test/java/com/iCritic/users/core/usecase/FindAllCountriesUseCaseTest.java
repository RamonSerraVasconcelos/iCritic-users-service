package com.iCritic.users.core.usecase;

import com.iCritic.users.core.fixture.CountryFixture;
import com.iCritic.users.core.model.Country;
import com.iCritic.users.core.usecase.boundary.FindAllCountriesBoundary;
import com.iCritic.users.core.usecase.boundary.FindCountriesCachedBoundary;
import com.iCritic.users.core.usecase.boundary.SaveCountriesToCacheBoundary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindAllCountriesUseCaseTest {

    @InjectMocks
    private FindAllCountriesUseCase findAllCountriesUseCase;

    @Mock
    private FindAllCountriesBoundary findAllCountriesBoundary;

    @Mock
    private FindCountriesCachedBoundary findCountriesCachedBoundary;

    @Mock
    private SaveCountriesToCacheBoundary saveCountriesToCacheBoundary;

    @Mock
    private Pageable pageable;

    @Test
    void givenExecution_whenCountrisAreNotOnCache_thenReturnCountriesFromDb() {
        var countries = List.of(CountryFixture.load(), CountryFixture.load());
        Page<Country> countryPage = new PageImpl<>(countries, pageable, 3);

        when(findCountriesCachedBoundary.execute(pageable)).thenReturn(null);
        when(findAllCountriesBoundary.execute(pageable)).thenReturn(countryPage);

        var returnedCountries = findAllCountriesUseCase.execute(pageable);

        verify(findAllCountriesBoundary).execute(pageable);
        verify(saveCountriesToCacheBoundary).execute(countryPage);

        assertThat(returnedCountries).isNotNull().isNotEmpty();
        assertThat(returnedCountries.getTotalElements()).isEqualTo(3);
        assertThat(returnedCountries.getContent().get(0)).usingRecursiveComparison().isEqualTo(countries.get(0));
    }

    @Test
    void givenExecution_whenCountriesAreOnCache_thenReturnCountriesFromCache() {
        var countries = List.of(CountryFixture.load(), CountryFixture.load());
        Page<Country> countryPage = new PageImpl<>(countries, pageable, 3);

        when(findCountriesCachedBoundary.execute(pageable)).thenReturn(countryPage);

        var returnedCountries = findAllCountriesUseCase.execute(pageable);

        verify(findCountriesCachedBoundary).execute(pageable);
        verifyNoInteractions(findAllCountriesBoundary);
        verifyNoInteractions(saveCountriesToCacheBoundary);

        assertThat(returnedCountries).isNotNull().isNotEmpty();
        assertThat(returnedCountries.getTotalElements()).isEqualTo(3);
        assertThat(returnedCountries.getContent().get(0)).usingRecursiveComparison().isEqualTo(countries.get(0));
    }
}