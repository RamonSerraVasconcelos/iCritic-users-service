package com.iCritic.iCritic.dataprovider.gateway.database.mapper;

import com.iCritic.iCritic.core.model.Country;
import com.iCritic.iCritic.dataprovider.gateway.database.entity.CountryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class CountryEntityMapper {

    public static final CountryEntityMapper INSTANCE = Mappers.getMapper(CountryEntityMapper.class);

    public abstract Country countryEntityToCountry(CountryEntity countryEntity);
}
