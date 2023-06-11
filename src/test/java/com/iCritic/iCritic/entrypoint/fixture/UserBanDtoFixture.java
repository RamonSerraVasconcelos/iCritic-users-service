package com.iCritic.iCritic.entrypoint.fixture;

import com.iCritic.iCritic.entrypoint.model.UserBanDto;

public class UserBanDtoFixture {

    public static UserBanDto load() {
        return UserBanDto.builder()
                .motive("motive")
                .build();
    }
}
