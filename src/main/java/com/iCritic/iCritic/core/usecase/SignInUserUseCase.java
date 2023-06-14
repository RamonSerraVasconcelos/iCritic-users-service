package com.iCritic.iCritic.core.usecase;

import com.iCritic.iCritic.core.model.User;
import com.iCritic.iCritic.core.usecase.boundary.FindUserByEmailBoundary;
import com.iCritic.iCritic.core.usecase.boundary.FindUserByIdBoundary;
import com.iCritic.iCritic.entrypoint.model.AuthorizationData;
import com.iCritic.iCritic.entrypoint.validation.JwtGenerator;
import com.iCritic.iCritic.exception.ResourceNotFoundException;
import com.iCritic.iCritic.exception.ResourceViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import static java.util.Objects.nonNull;

@Component
@RequiredArgsConstructor
@Slf4j
public class SignInUserUseCase {

    private final FindUserByEmailBoundary findUserByEmailBoundary;

    private final BCryptPasswordEncoder bcrypt;

    public boolean execute(User userData) {
        User user = findUserByEmailBoundary.execute(userData.getEmail());

        if(!nonNull(user)) {
            throw new ResourceViolationException("Invalid email or password");
        }

        boolean isPasswordValid = bcrypt.matches(userData.getPassword(), user.getPassword());

        if(!isPasswordValid) {
            throw new ResourceViolationException("Invalid email or password");
        }

        return true;
    }
}
