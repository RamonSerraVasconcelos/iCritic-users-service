package com.iCritic.users.dataprovider.gateway.database.impl;

import com.iCritic.users.core.model.Country;
import com.iCritic.users.core.usecase.boundary.FindCountryByIdBoundary;
import com.iCritic.users.dataprovider.gateway.database.mapper.CountryEntityMapper;
import com.iCritic.users.dataprovider.gateway.database.repository.CountryRepository;
import com.iCritic.users.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class FindCountryByIdGateway implements FindCountryByIdBoundary {

    private final CountryRepository countryRepository;

    public Country execute(Long id) {
        return CountryEntityMapper.INSTANCE.countryEntityToCountry(countryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Country not found")));
    }
}
