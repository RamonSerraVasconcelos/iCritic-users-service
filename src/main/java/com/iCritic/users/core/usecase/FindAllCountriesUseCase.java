package com.iCritic.users.core.usecase;

import com.iCritic.users.core.model.Country;
import com.iCritic.users.core.usecase.boundary.FindAllCountriesBoundary;
import com.iCritic.users.core.usecase.boundary.FindCountriesCachedBoundary;
import com.iCritic.users.core.usecase.boundary.SaveCountriesToCacheBoundary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class FindAllCountriesUseCase {

    private final FindAllCountriesBoundary findAllCountriesBoundary;

    private final FindCountriesCachedBoundary findCountriesCachedBoundary;

    private final SaveCountriesToCacheBoundary saveCountriesToCacheBoundary;

    public Page<Country> execute(Pageable pageable) {
        log.info("Finding all countries");

        Page<Country> cachedCountries = findCountriesCachedBoundary.execute(pageable);

        if (nonNull(cachedCountries)) {
            return cachedCountries;
        }

        Page<Country> countries = findAllCountriesBoundary.execute(pageable);

        saveCountriesToCacheBoundary.execute(countries);

        return countries;
    }
}
