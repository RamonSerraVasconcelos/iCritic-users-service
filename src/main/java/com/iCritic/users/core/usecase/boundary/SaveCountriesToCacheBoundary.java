package com.iCritic.users.core.usecase.boundary;

import com.iCritic.users.core.model.Country;
import org.springframework.data.domain.Page;

public interface SaveCountriesToCacheBoundary {

    void execute(Page<Country> countries);
}
