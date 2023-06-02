package com.iCritic.iCritic.core.fixture;

import com.iCritic.iCritic.core.model.Country;

public class CountryFixture {

    public static Country load() {
        return Country.builder()
                .name("test country")
                .build();
    }
}
