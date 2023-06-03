package com.iCritic.iCritic.dataprovider.gateway.database.mapper;


import com.iCritic.iCritic.core.fixture.UserFixture;
import com.iCritic.iCritic.core.model.User;
import com.iCritic.iCritic.dataprovider.gateway.database.entity.UserEntity;
import com.iCritic.iCritic.dataprovider.gateway.database.fixture.UserEntityFixture;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserEntityMapperTest {

    @Test
    void givenUser_convertUserToUserEntity_thenReturnUserEntity() {
        User user = UserFixture.load();

        UserEntity userEntity = UserEntityMapper.INSTANCE.userToUserEntity(user);

        assertNotNull(userEntity);
        assertEquals(userEntity.getId(), user.getId());
        assertEquals(userEntity.getName(), user.getName());
        assertEquals(userEntity.getEmail(), user.getEmail());
        assertEquals(userEntity.getDescription(), user.getDescription());
        assertEquals(userEntity.getPassword(), user.getPassword());
        assertEquals(userEntity.isActive(), user.isActive());
        assertEquals(userEntity.getRole(), user.getRole());
        assertEquals(userEntity.getCountryId(), user.getCountryId());
        assertEquals(userEntity.getCountry().getName(), user.getCountry().getName());
        assertEquals(userEntity.getCreatedAt(), user.getCreatedAt());
    }

    @Test
    void givenUserEntity_convertUserEntityToUser_thenReturnUser() {
        UserEntity userEntity = UserEntityFixture.load();

        User user = UserEntityMapper.INSTANCE.userEntityToUser(userEntity);

        assertNotNull(user);
        assertEquals(user.getName(), userEntity.getName());
        assertEquals(user.getEmail(), userEntity.getEmail());
        assertEquals(user.getDescription(), userEntity.getDescription());
        assertEquals(user.getPassword(), userEntity.getPassword());
        assertEquals(user.isActive(), userEntity.isActive());
        assertEquals(user.getRole(), userEntity.getRole());
        assertEquals(user.getCountryId(), userEntity.getCountryId());
        assertEquals(user.getCountry().getName(), userEntity.getCountry().getName());
        assertEquals(user.getCreatedAt(), userEntity.getCreatedAt());
    }
}
