package com.iCritic.users.dataprovider.gateway.database.impl;

import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.UpdateUserBoundary;
import com.iCritic.users.dataprovider.gateway.database.entity.UserEntity;
import com.iCritic.users.dataprovider.gateway.database.mapper.UserEntityMapper;
import com.iCritic.users.dataprovider.gateway.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UpdateUserGateway implements UpdateUserBoundary {

    private final UserRepository userRepository;

    public User execute(User user) {
        UserEntityMapper mapper = UserEntityMapper.INSTANCE;
        UserEntity userEntity = mapper.userToUserEntity(user);

        UserEntity updatedUser = userRepository.save(userEntity);

        return mapper.userEntityToUser(updatedUser);
    }
}
