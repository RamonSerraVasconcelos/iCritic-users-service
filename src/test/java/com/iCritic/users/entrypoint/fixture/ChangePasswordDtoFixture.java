package com.iCritic.users.entrypoint.fixture;

import com.iCritic.users.entrypoint.entity.ChangePasswordDto;

public class ChangePasswordDtoFixture {

    public static ChangePasswordDto load() {
        return ChangePasswordDto.builder()
                .password("password")
                .newPassword("newPassword")
                .confirmPassword("newPassword")
                .build();
    }
}
