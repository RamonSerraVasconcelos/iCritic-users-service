package com.iCritic.users.core.usecase;

import com.iCritic.users.core.enums.NotificationBodyEnum;
import com.iCritic.users.core.enums.NotificationIdsEnum;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.FindUserByEmailBoundary;
import com.iCritic.users.core.usecase.boundary.UpdateUserBoundary;
import com.iCritic.users.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetRequestUseCase {

    private final FindUserByEmailBoundary findUserByEmailBoundary;

    private final UpdateUserBoundary updateUserBoundary;

    private final SendEmailNotificationUseCase sendEmailNotificationUseCase;

    private final BCryptPasswordEncoder bcrypt;

    private static final String NOTIFICATION_SUBJECT = "Password Reset Request";

    public void execute(String email) {
        try {
            User user = findUserByEmailBoundary.execute(email);

            if (isNull(user)) {
                throw new ResourceNotFoundException("User not found");
            }

            String passwordResetHash = UUID.randomUUID().toString();
            String encryptedHash = bcrypt.encode(passwordResetHash);

            user.setPasswordResetHash(encryptedHash);
            user.setPasswordResetDate(LocalDateTime.now().plusMinutes(5));

            updateUserBoundary.execute(user);

            Map<String, String> notificationBodyVariables = new HashMap<>();
            notificationBodyVariables.put("passwordResetHash", passwordResetHash);

            sendEmailNotificationUseCase.execute(user.getId(), user.getEmail(), NotificationIdsEnum.PASSWORD_RESET_REQUEST.getNotificationId(),
                    NOTIFICATION_SUBJECT, NotificationBodyEnum.PASSWORD_RESET_REQUEST, notificationBodyVariables);
        } catch (ResourceNotFoundException e) {
            log.error("User not found with email: {}", email);
        }
    }
}
