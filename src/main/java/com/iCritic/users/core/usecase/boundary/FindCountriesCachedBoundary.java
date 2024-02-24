package com.iCritic.users.core.usecase.boundary;

import com.iCritic.users.core.model.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindCountriesCachedBoundary {

    Page<Country> execute(Pageable pageable);
}
