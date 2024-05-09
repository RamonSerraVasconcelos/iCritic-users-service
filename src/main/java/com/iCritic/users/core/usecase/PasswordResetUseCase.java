package com.iCritic.users.core.usecase;

import com.iCritic.users.core.enums.NotificationBodyEnum;
import com.iCritic.users.core.enums.NotificationIdsEnum;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.DeleteUserRefreshTokensBoundary;
import com.iCritic.users.core.usecase.boundary.FindUserByEmailBoundary;
import com.iCritic.users.core.usecase.boundary.UpdateUserBoundary;
import com.iCritic.users.exception.ResourceViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetUseCase {

    private final FindUserByEmailBoundary findUserByEmailBoundary;

    private final UpdateUserBoundary updateUserBoundary;

    private final DeleteUserRefreshTokensBoundary deleteUserRefreshTokensBoundary;

    private final SendEmailNotificationUseCase sendEmailNotificationUseCase;

    private final BCryptPasswordEncoder bcrypt;

    private static final String NOTIFICATION_SUBJECT = "Password reset notification";

    public void execute(String email, String passwordResetHash, String newPassword) {
        try {
            User user = findUserByEmailBoundary.execute(email);

            if (isNull(user)) {
                throw new ResourceViolationException("Invalid email or password hash");
            }

            boolean isPasswordResetHashValid = bcrypt.matches(passwordResetHash, user.getPasswordResetHash());

            if (!isPasswordResetHashValid) {
                throw new ResourceViolationException("Invalid email or password hash");
            }

            if (LocalDateTime.now().isAfter(user.getPasswordResetDate())) {
                throw new ResourceViolationException("Invalid email or password hash");
            }

            boolean isPasswordDuplicated = bcrypt.matches(newPassword, user.getPassword());

            if (isPasswordDuplicated) {
                throw new ResourceViolationException("The new password cannot be equal to the old one");
            }

            String encodedPassword = bcrypt.encode(newPassword);
            user.setPassword(encodedPassword);
            user.setPasswordResetHash(null);
            user.setPasswordResetDate(null);

            updateUserBoundary.execute(user);
            deleteUserRefreshTokensBoundary.execute(user.getId());

            sendEmailNotificationUseCase.execute(user.getId(), user.getEmail(), NotificationIdsEnum.PASSWORD_RESET.getNotificationId(),
                    NOTIFICATION_SUBJECT, NotificationBodyEnum.PASSWORD_RESET, null);
        } catch (ResourceViolationException e) {
            log.error("Failed to reset user password with email: [{}]", email, e);
            throw e;
        }
    }
}
