package com.iCritic.users.core.usecase.boundary;

import com.iCritic.users.core.model.Country;

import java.util.List;

public interface FindAllCountriesBoundary {

    List<Country> execute();
}
