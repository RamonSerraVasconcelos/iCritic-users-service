package com.iCritic.iCritic.core.usecase.boundary;

import com.iCritic.iCritic.core.model.Country;

public interface FindCountryByIdBoundary {

    Country execute(Long id);
}
