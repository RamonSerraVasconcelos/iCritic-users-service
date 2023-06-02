package com.iCritic.iCritic.core.fixture;

import com.iCritic.iCritic.core.enums.Role;
import com.iCritic.iCritic.core.model.User;

import java.time.LocalDateTime;

public class UserFixture {

    public static User load() {
        return User.builder()
                .name("test")
                .email("test@test.test")
                .description("test description")
                .password("test")
                .active(true)
                .role(Role.DEFAULT)
                .countryId(1L)
                .country(CountryFixture.load())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
