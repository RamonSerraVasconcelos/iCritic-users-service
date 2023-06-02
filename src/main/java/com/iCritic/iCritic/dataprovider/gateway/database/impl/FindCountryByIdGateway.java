package com.iCritic.iCritic.dataprovider.gateway.database.impl;

import com.iCritic.iCritic.core.model.Country;
import com.iCritic.iCritic.core.usecase.boundary.FindCountryByIdBoundary;
import com.iCritic.iCritic.dataprovider.gateway.database.mapper.CountryEntityMapper;
import com.iCritic.iCritic.dataprovider.gateway.database.repository.CountryRepository;
import com.iCritic.iCritic.exception.ResourceNotFoundException;
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
