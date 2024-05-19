package com.iCritic.users.core.usecase;

import com.iCritic.users.core.enums.Role;
import com.iCritic.users.core.model.Country;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.CreateUserBoundary;
import com.iCritic.users.core.usecase.boundary.FindCountryByIdBoundary;
import com.iCritic.users.core.usecase.boundary.FindUserByEmailBoundary;
import com.iCritic.users.core.usecase.boundary.InvalidateUsersCacheBoundary;
import com.iCritic.users.exception.ResourceConflictException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateUserUseCase {

    private final CreateUserBoundary createUserBoundary;

    private final FindUserByEmailBoundary findUserByEmailBoundary;

    private final FindCountryByIdBoundary findCountryByIdBoundary;

    private final InvalidateUsersCacheBoundary invalidateUsersCacheBoundary;

    private final BCryptPasswordEncoder bcrypt;

    public User execute(User user) {
        log.info("Creating user with name: [{}]", user.getName());

        User isUserDuplicated = findUserByEmailBoundary.execute(user.getEmail());

        if (nonNull(isUserDuplicated)) {
            throw new ResourceConflictException("User email already exists");
        }

        Country country = findCountryByIdBoundary.execute(user.getCountryId());
        user.setCountry(country);

        String encodedPassword = bcrypt.encode(user.getPassword());
        user.setPassword(encodedPassword);

        user.setActive(true);
        user.setRole(Role.DEFAULT);

        User createdUser = createUserBoundary.execute(user);

        invalidateUsersCacheBoundary.execute();

        return createdUser;
    }
}
