package com.iCritic.users.core.usecase;

import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.FindUsersBoundary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class FindUsersUseCase {

    private final FindUsersBoundary findUsersBoundary;

    public Page<User> execute(Pageable pageable) {
        log.info("Finding all users");

        return findUsersBoundary.execute(pageable);
    }
}
