package com.iCritic.users.dataprovider.gateway.database.fixture;

import com.iCritic.users.dataprovider.gateway.database.entity.CountryEntity;

public class CountryEntityFixture {

    public static CountryEntity load() {
        return CountryEntity.builder()
                .id(1L)
                .name("test")
                .build();
    }
}
