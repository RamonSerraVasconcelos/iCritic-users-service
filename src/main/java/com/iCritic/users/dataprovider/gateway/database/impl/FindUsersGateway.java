package com.iCritic.users.dataprovider.gateway.database.impl;

import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.FindUsersBoundary;
import com.iCritic.users.dataprovider.gateway.database.entity.UserEntity;
import com.iCritic.users.dataprovider.gateway.database.mapper.UserEntityMapper;
import com.iCritic.users.dataprovider.gateway.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class FindUsersGateway implements FindUsersBoundary {

    private final UserRepository userRepository;

    public Page<User> execute(Pageable pageable) {
        UserEntityMapper mapper = UserEntityMapper.INSTANCE;

        Page<UserEntity> pageableUsers = userRepository.findAllByOrderByIdAsc(pageable);

        List<User> users = pageableUsers.stream().map(mapper::userEntityToUser).collect(Collectors.toList());

        return new PageImpl<>(users, pageable, pageableUsers.getTotalElements());
    }
}
