package com.iCritic.users.entrypoint.fixture;

import com.iCritic.users.entrypoint.model.UserBanDto;

public class UserBanDtoFixture {

    public static UserBanDto load() {
        return UserBanDto.builder()
                .motive("motive")
                .build();
    }
}
