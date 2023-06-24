package com.iCritic.users.core.usecase;

import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.FindUserByIdBoundary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FindUserByIdUseCase {

    private final FindUserByIdBoundary findUserByIdBoundary;

    public User execute(Long id) {
        return findUserByIdBoundary.execute(id);
    }
}
