package com.iCritic.users.core.usecase;

import com.iCritic.users.core.fixture.CountryFixture;
import com.iCritic.users.core.model.Country;
import com.iCritic.users.core.usecase.boundary.FindCountryByIdBoundary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindCountryByIdUseCaseTest {

    @InjectMocks
    private FindCountryByIdUseCase findCountryByIdUseCase;

    @Mock
    private FindCountryByIdBoundary findCountryByIdBoundary;

    @Test
    void givenValidParameter_thenFind_andReturnCountry() {
        Country country = CountryFixture.load();

        when(findCountryByIdBoundary.execute(country.getId())).thenReturn(country);

        Country returnedCountry = findCountryByIdUseCase.execute(country.getId());

        verify(findCountryByIdBoundary).execute(country.getId());
        assertThat(returnedCountry).isNotNull().usingRecursiveComparison().isEqualTo(country);
    }
}