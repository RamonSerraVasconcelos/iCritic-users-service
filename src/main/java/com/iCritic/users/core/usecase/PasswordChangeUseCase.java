package com.iCritic.users.core.usecase;

import com.iCritic.users.core.enums.NotificationBodyEnum;
import com.iCritic.users.core.enums.NotificationIdsEnum;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.DeleteUserRefreshTokensBoundary;
import com.iCritic.users.core.usecase.boundary.FindUserByIdBoundary;
import com.iCritic.users.core.usecase.boundary.UpdateUserBoundary;
import com.iCritic.users.exception.ResourceViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordChangeUseCase {

    private final FindUserByIdBoundary findUserByIdBoundary;

    private final UpdateUserBoundary updateUserBoundary;

    private final DeleteUserRefreshTokensBoundary deleteUserRefreshTokensBoundary;

    private final SendEmailNotificationUseCase sendEmailNotificationUseCase;

    private final BCryptPasswordEncoder bcrypt;

    private static final String NOTIFICATION_SUBJECT = "Password Reset Request";

    public void execute(Long userId, String currentPassword, String newPassword, String newPasswordConfirmation) {
        try {
            log.info("Changing password for user with id: [{}]", userId);

            User user = findUserByIdBoundary.execute(userId);

            boolean isCurrentPasswordValid = bcrypt.matches(currentPassword, user.getPassword());

            if (!isCurrentPasswordValid) {
                throw new ResourceViolationException("Invalid password");
            }

            boolean isPasswordDuplicated = bcrypt.matches(newPassword, user.getPassword());

            if (isPasswordDuplicated) {
                throw new ResourceViolationException("The new password cannot be equal to the old one");
            }

            if (!newPassword.equals(newPasswordConfirmation)) {
                throw new ResourceViolationException("Password confirmation does not match");
            }

            user.setPassword(bcrypt.encode(newPassword));

            updateUserBoundary.execute(user);
            deleteUserRefreshTokensBoundary.execute(user.getId());

            sendEmailNotificationUseCase.execute(user.getId(), user.getEmail(), NotificationIdsEnum.PASSWORD_CHANGE.getNotificationId(),
                    NOTIFICATION_SUBJECT, NotificationBodyEnum.PASSWORD_CHANGE, null);
        } catch (Exception e) {
            log.error("Error changing password for user with id: [{}]", userId, e);
            throw e;
        }
    }
}
