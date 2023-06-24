package com.iCritic.users.dataprovider.gateway.database.impl;

import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.CreateUserBoundary;
import com.iCritic.users.dataprovider.gateway.database.entity.UserEntity;
import com.iCritic.users.dataprovider.gateway.database.mapper.UserEntityMapper;
import com.iCritic.users.dataprovider.gateway.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CreateUserGateway implements CreateUserBoundary {

    private final UserRepository userRepository;

    public User execute(User user) {
        UserEntityMapper mapper = UserEntityMapper.INSTANCE;
        UserEntity userEntity = mapper.userToUserEntity(user);

        return mapper.userEntityToUser(userRepository.save(userEntity));
    }
}
