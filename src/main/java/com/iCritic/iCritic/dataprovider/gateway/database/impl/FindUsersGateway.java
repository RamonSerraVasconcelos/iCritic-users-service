package com.iCritic.iCritic.dataprovider.gateway.database.impl;

import com.iCritic.iCritic.core.model.User;
import com.iCritic.iCritic.core.usecase.boundary.FindUsersBoundary;
import com.iCritic.iCritic.dataprovider.gateway.database.mapper.UserEntityMapper;
import com.iCritic.iCritic.dataprovider.gateway.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class FindUsersGateway implements FindUsersBoundary {

    private final UserRepository userRepository;

    public List<User> execute() {
        return userRepository.findAll().stream().map(UserEntityMapper.INSTANCE::userEntityToUser).collect(Collectors.toList());
    }
}
