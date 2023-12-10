package com.iCritic.users.entrypoint.mapper;

import com.iCritic.users.core.model.Country;
import com.iCritic.users.entrypoint.entity.CountryResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class CountryDtoMapper {

    public static final CountryDtoMapper INSTANCE = Mappers.getMapper(CountryDtoMapper.class);

    public abstract CountryResponseDto countryToCountryResponseDto(Country country);
}
