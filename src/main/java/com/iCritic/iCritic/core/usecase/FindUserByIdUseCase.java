package com.iCritic.iCritic.core.usecase;

import com.iCritic.iCritic.core.model.User;
import com.iCritic.iCritic.core.usecase.boundary.FindUserByIdBoundary;
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
