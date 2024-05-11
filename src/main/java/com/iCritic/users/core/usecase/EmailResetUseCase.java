package com.iCritic.users.core.usecase;

import com.iCritic.users.core.enums.NotificationContentEnum;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.UpdateUserBoundary;
import com.iCritic.users.exception.ResourceNotFoundException;
import com.iCritic.users.exception.ResourceViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailResetUseCase {

    private final FindUserByIdUseCase findUserByIdUseCase;

    private final UpdateUserBoundary updateUserBoundary;

    private final SendEmailNotificationUseCase sendEmailNotificationUseCase;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void execute(Long userId, String emailResetHash) {
        try {
            log.info("Resetting email for user with id: [{}]", userId);

            User user = findUserByIdUseCase.execute(userId);

            boolean isEmailResetHashValid = bCryptPasswordEncoder.matches(emailResetHash, user.getEmailResetHash());

            if (!isEmailResetHashValid) {
                throw new ResourceViolationException("Invalid request data");
            }

            if (LocalDateTime.now().isAfter(user.getEmailResetDate())) {
                throw new ResourceViolationException("Your password reset link has expired");
            }

            user.setEmail(user.getNewEmailReset());
            user.setEmailResetHash(null);
            user.setEmailResetDate(null);

            updateUserBoundary.execute(user);

            log.info("Finished replacing email: [{}] for user with id: [{}]", user.getEmail(), user.getId());

            sendEmailNotificationUseCase.execute(user.getId(), user.getEmail(), NotificationContentEnum.EMAIL_RESET, null);
        } catch (ResourceNotFoundException e) {
            log.error("Error when resetting email for user with id: [{}]", userId, e);
            throw new ResourceViolationException("Invalid request data");
        }
    }
}
