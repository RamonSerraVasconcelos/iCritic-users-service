package com.iCritic.users.core.usecase;

import com.iCritic.users.core.model.Country;
import com.iCritic.users.core.usecase.boundary.FindCountryByIdBoundary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FindCountryByIdUseCase {

    private final FindCountryByIdBoundary findCountryByIdBoundary;

    public Country execute(Long id) {
        try {
            log.info("Finding country with id: [{}]", id);

            return findCountryByIdBoundary.execute(id);
        } catch (Exception e) {
            log.error("Error finding country with id: [{}]", id, e);
            throw e;
        }
    }
}
