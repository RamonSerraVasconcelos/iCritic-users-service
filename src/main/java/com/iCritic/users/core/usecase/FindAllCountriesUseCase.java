package com.iCritic.users.core.usecase;

import com.iCritic.users.core.model.Country;
import com.iCritic.users.core.usecase.boundary.FindAllCountriesBoundary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class FindAllCountriesUseCase {

    private final FindAllCountriesBoundary findAllCountriesBoundary;

    public List<Country> execute(){
        log.info("Finding all countries");
        return findAllCountriesBoundary.execute();
    }
}
