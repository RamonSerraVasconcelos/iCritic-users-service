package com.iCritic.users.core.usecase.boundary;

import com.iCritic.users.core.model.Country;

public interface FindCountryByIdBoundary {

    Country execute(Long id);
}
