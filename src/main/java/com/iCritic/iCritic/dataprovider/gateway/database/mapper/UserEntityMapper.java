package com.iCritic.iCritic.dataprovider.gateway.database.mapper;

import com.iCritic.iCritic.core.model.User;
import com.iCritic.iCritic.dataprovider.gateway.database.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class UserEntityMapper {

    public static final UserEntityMapper INSTANCE = Mappers.getMapper(UserEntityMapper.class);

    public abstract UserEntity userToUserEntity(User user);

    public abstract User userEntityToUser(UserEntity userEntity);
}
