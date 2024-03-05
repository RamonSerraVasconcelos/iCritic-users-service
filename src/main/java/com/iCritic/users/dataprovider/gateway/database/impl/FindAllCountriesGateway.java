package com.iCritic.users.dataprovider.gateway.database.impl;

import com.iCritic.users.core.model.Country;
import com.iCritic.users.core.usecase.boundary.FindAllCountriesBoundary;
import com.iCritic.users.dataprovider.gateway.database.entity.CountryEntity;
import com.iCritic.users.dataprovider.gateway.database.mapper.CountryEntityMapper;
import com.iCritic.users.dataprovider.gateway.database.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FindAllCountriesGateway implements FindAllCountriesBoundary {

    private final CountryRepository countryRepository;

    public Page<Country> execute(Pageable pageable) {
        CountryEntityMapper countryEntityMapper = CountryEntityMapper.INSTANCE;

        Page<CountryEntity> pageableCountries = countryRepository.findAllByOrderByIdAsc(pageable);

        List<Country> countries = pageableCountries
                .stream()
                .map(countryEntityMapper::countryEntityToCountry)
                .collect(Collectors.toList());

        return new PageImpl<>(countries, pageable, pageableCountries.getTotalElements());
    }
}
