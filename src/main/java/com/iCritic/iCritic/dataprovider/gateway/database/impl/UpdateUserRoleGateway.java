package com.iCritic.iCritic.dataprovider.gateway.database.impl;

import com.iCritic.iCritic.core.enums.Role;
import com.iCritic.iCritic.core.usecase.boundary.UpdateUserRoleBoundary;
import com.iCritic.iCritic.dataprovider.gateway.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UpdateUserRoleGateway implements UpdateUserRoleBoundary {

    private final UserRepository userRepository;

    public void execute(Long id, Role role) {
        userRepository.updateRole(id, role);
    }
}
