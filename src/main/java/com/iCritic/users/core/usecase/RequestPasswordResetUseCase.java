package com.iCritic.users.core.usecase;

import com.iCritic.users.core.model.RequestPasswordResetMessage;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.FindUserByEmailBoundary;
import com.iCritic.users.core.usecase.boundary.FindUserByIdBoundary;
import com.iCritic.users.core.usecase.boundary.PostRequestPasswordResetMessage;
import com.iCritic.users.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

import static java.util.Objects.isNull;

@Component
@RequiredArgsConstructor
@Slf4j
public class RequestPasswordResetUseCase {

    private final FindUserByEmailBoundary findUserByEmailBoundary;

    private final PostRequestPasswordResetMessage postRequestPasswordResetMessage;

    private final BCryptPasswordEncoder bcrypt;

    public void execute(String email) {
        try {
            User user = findUserByEmailBoundary.execute(email);

            if(isNull(user)) {
                throw new ResourceNotFoundException("User not found");
            }

            String passwordResetHash = UUID.randomUUID().toString();
            String encryptedHash = bcrypt.encode(passwordResetHash);

            user.setPasswordResetHash(encryptedHash);
            user.setPasswordResetDate(new Date());

            RequestPasswordResetMessage requestPasswordResetMessage = RequestPasswordResetMessage.builder()
                    .email(user.getEmail())
                    .passwordResetHash(encryptedHash)
                    .build();

            postRequestPasswordResetMessage.execute(requestPasswordResetMessage);
        } catch(ResourceNotFoundException e) {
            log.error("User not found with email: {}", email);
        }
    }
}
