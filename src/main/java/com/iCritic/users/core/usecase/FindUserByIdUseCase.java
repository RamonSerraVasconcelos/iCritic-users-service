package com.iCritic.users.core.usecase;

import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.FindUserByIdBoundary;
import com.iCritic.users.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FindUserByIdUseCase {

    private final FindUserByIdBoundary findUserByIdBoundary;

    public User execute(Long id) {
        try {
            return findUserByIdBoundary.execute(id);
        } catch (ResourceNotFoundException e) {
            log.error("User with id: [{}] not found", id, e);
            throw e;
        }
    }
}
