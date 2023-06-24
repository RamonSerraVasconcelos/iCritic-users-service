package com.iCritic.users.dataprovider.gateway.database.impl;

import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.FindUserByIdBoundary;
import com.iCritic.users.dataprovider.gateway.database.mapper.UserEntityMapper;
import com.iCritic.users.dataprovider.gateway.database.repository.UserRepository;
import com.iCritic.users.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class FindUserByIdGateway implements FindUserByIdBoundary {

    private final UserRepository userRepository;

    public User execute(Long id) {
        return UserEntityMapper.INSTANCE.userEntityToUser(userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found")));
    }
}
