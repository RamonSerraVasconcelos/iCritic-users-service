package com.iCritic.users.entrypoint.resource;

import com.iCritic.users.core.model.Country;
import com.iCritic.users.core.usecase.FindAllCountriesUseCase;
import com.iCritic.users.core.usecase.FindCountryByIdUseCase;
import com.iCritic.users.entrypoint.entity.CountryResponseDto;
import com.iCritic.users.entrypoint.mapper.CountryDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/countries")
@RequiredArgsConstructor
public class CountryResource {

    private final FindAllCountriesUseCase findAllCountriesUseCase;

    private final FindCountryByIdUseCase findCountryByIdUseCase;

    @GetMapping
    public List<CountryResponseDto> loadAll() {
        return findAllCountriesUseCase.execute().stream().map(CountryDtoMapper.INSTANCE::countryToCountryResponseDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CountryResponseDto> findById(@PathVariable Long id) {
        Country country = findCountryByIdUseCase.execute(id);

        return ResponseEntity.status(HttpStatus.OK).body(CountryDtoMapper.INSTANCE.countryToCountryResponseDto(country));
    }
}
