package com.iCritic.iCritic.dataprovider.gateway.database.fixture;

import com.iCritic.iCritic.core.enums.Role;
import com.iCritic.iCritic.core.fixture.CountryFixture;
import com.iCritic.iCritic.dataprovider.gateway.database.entity.UserEntity;

import java.time.LocalDateTime;

public class UserEntityFixture {

    public static UserEntity load() {
        return UserEntity.builder()
                .id(1L)
                .name("test")
                .email("test@test.test")
                .description("test description")
                .password("test")
                .active(true)
                .role(Role.DEFAULT)
                .countryId(1L)
                .country(CountryEntityFixture.load())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
