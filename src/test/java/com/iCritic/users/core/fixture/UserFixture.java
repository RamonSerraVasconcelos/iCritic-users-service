package com.iCritic.users.core.fixture;

import com.iCritic.users.core.enums.Role;
import com.iCritic.users.core.model.User;

import java.time.LocalDateTime;

public class UserFixture {

    public static User load() {
        return User.builder()
                .id(1L)
                .name("test")
                .email("test@test.test")
                .description("test description")
                .password("test")
                .active(true)
                .role(Role.DEFAULT)
                .countryId(1L)
                .profilePicture(ImageFixture.load())
                .country(CountryFixture.load())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
