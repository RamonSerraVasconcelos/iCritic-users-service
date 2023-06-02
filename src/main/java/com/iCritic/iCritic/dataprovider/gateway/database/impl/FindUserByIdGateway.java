package com.iCritic.iCritic.dataprovider.gateway.database.impl;

import com.iCritic.iCritic.core.model.User;
import com.iCritic.iCritic.core.usecase.boundary.FindCountryByIdBoundary;
import com.iCritic.iCritic.core.usecase.boundary.FindUserByIdBoundary;
import com.iCritic.iCritic.dataprovider.gateway.database.mapper.UserEntityMapper;
import com.iCritic.iCritic.dataprovider.gateway.database.repository.UserRepository;
import com.iCritic.iCritic.exception.ResourceNotFoundException;
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
