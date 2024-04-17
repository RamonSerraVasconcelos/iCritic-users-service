package com.iCritic.users.core.usecase;

import com.iCritic.users.core.model.EmailResetRequest;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.PostEmailResetRequestMessageBoundary;
import com.iCritic.users.core.usecase.boundary.UpdateUserBoundary;
import com.iCritic.users.exception.ResourceConflictException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailResetRequestUseCase {

    private final FindUserByIdUseCase findUserByIdUseCase;

    private final UpdateUserBoundary updateUserBoundary;

    private final PostEmailResetRequestMessageBoundary postEmailResetRequestMessageBoundary;

    private final BCryptPasswordEncoder bcrypt;

    public void execute(Long id, String newEmail) {
        log.info("Requesting email reset for user with id: [{}]", id);

        User user = findUserByIdUseCase.execute(id);

        if (user.getEmail().equals(newEmail)) {
            log.info("User with id: [{}] already has the new email: [{}]", id, newEmail);
            throw new ResourceConflictException("New email must be different from the current one");
        }

        String emailResetHash = UUID.randomUUID().toString();
        String encryptedHash = bcrypt.encode(emailResetHash);

        user.setEmailResetHash(encryptedHash);
        user.setEmailResetDate(LocalDateTime.now().plusMinutes(15));
        user.setNewEmailReset(newEmail);

        updateUserBoundary.execute(user);

        EmailResetRequest emailResetRequest = EmailResetRequest.builder()
                        .userId(user.getId())
                        .email(user.getNewEmailReset())
                        .emailResetHash(emailResetHash)
                        .build();

        postEmailResetRequestMessageBoundary.execute(emailResetRequest);
    }
}
