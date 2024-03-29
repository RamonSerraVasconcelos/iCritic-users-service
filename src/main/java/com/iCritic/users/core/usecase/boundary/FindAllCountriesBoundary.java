package com.iCritic.users.core.usecase.boundary;

import com.iCritic.users.core.model.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FindAllCountriesBoundary {

    Page<Country> execute(Pageable pageable);
}
