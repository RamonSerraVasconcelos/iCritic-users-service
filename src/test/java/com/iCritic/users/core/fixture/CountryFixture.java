package com.iCritic.users.core.fixture;

import com.iCritic.users.core.model.Country;

public class CountryFixture {

    public static Country load() {
        return Country.builder()
                .id(1L)
                .name("test")
                .build();
    }
}
