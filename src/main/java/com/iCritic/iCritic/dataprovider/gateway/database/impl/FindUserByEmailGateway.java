package com.iCritic.iCritic.dataprovider.gateway.database.impl;

import com.iCritic.iCritic.core.model.User;
import com.iCritic.iCritic.core.usecase.boundary.FindUserByEmailBoundary;
import com.iCritic.iCritic.dataprovider.gateway.database.mapper.UserEntityMapper;
import com.iCritic.iCritic.dataprovider.gateway.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class FindUserByEmailGateway implements FindUserByEmailBoundary {

    private final UserRepository userRepository;

    public User execute(String email) {
        return UserEntityMapper.INSTANCE.userEntityToUser(userRepository.findByEmail(email));
    }
}
