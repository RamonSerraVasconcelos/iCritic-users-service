package com.iCritic.users.dataprovider.gateway.database.impl;

import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.FindUsersBoundary;
import com.iCritic.users.dataprovider.gateway.database.mapper.UserEntityMapper;
import com.iCritic.users.dataprovider.gateway.database.repository.UserRepository;
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
        return userRepository.findAllByOrderByIdAsc().stream().map(UserEntityMapper.INSTANCE::userEntityToUser).collect(Collectors.toList());
    }
}
