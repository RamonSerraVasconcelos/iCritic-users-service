package com.iCritic.users.core.usecase;

import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.FindUsersBoundary;
import com.iCritic.users.core.usecase.boundary.FindUsersCachedBoundary;
import com.iCritic.users.core.usecase.boundary.SaveUsersToCacheBoundary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static java.util.Objects.nonNull;

@Service
@Slf4j
@RequiredArgsConstructor
public class FindUsersUseCase {

    private final FindUsersBoundary findUsersBoundary;

    private final FindUsersCachedBoundary findUsersCachedBoundary;

    private final SaveUsersToCacheBoundary saveUsersToCacheBoundary;

    public Page<User> execute(Pageable pageable) {
        try {
            log.info("Finding all users");

            Page<User> cachedUsers = findUsersCachedBoundary.execute(pageable);

            if (nonNull(cachedUsers)) {
                return cachedUsers;
            }

            Page<User> users = findUsersBoundary.execute(pageable);
            saveUsersToCacheBoundary.execute(users);

            return users;
        } catch (Exception e) {
            log.error("Error when finding all users", e);
            throw e;
        }

    }
}
