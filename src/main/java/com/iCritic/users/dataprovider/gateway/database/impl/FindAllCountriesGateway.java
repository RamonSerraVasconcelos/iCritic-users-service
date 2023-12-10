package com.iCritic.users.dataprovider.gateway.database.impl;

import com.iCritic.users.core.model.Country;
import com.iCritic.users.core.usecase.boundary.FindAllCountriesBoundary;
import com.iCritic.users.dataprovider.gateway.database.mapper.CountryEntityMapper;
import com.iCritic.users.dataprovider.gateway.database.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FindAllCountriesGateway implements FindAllCountriesBoundary {

    private final CountryRepository countryRepository;

    public List<Country> execute() {
        CountryEntityMapper countryEntityMapper = CountryEntityMapper.INSTANCE;

        return countryRepository.findAll().stream().map(countryEntityMapper::countryEntityToCountry).collect(Collectors.toList());
    }
}
