package com.iCritic.users.core.usecase;

import com.iCritic.users.core.enums.NotificationBodyEnum;
import com.iCritic.users.core.enums.NotificationIdsEnum;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.FindUserByEmailBoundary;
import com.iCritic.users.core.usecase.boundary.UpdateUserBoundary;
import com.iCritic.users.exception.ResourceConflictException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailResetRequestUseCase {

    private final FindUserByIdUseCase findUserByIdUseCase;

    private final FindUserByEmailBoundary findUserByEmailBoundary;

    private final UpdateUserBoundary updateUserBoundary;

    private final SendEmailNotificationUseCase sendEmailNotificationUseCase;

    private final BCryptPasswordEncoder bcrypt;

    private final String NOTIFICATION_SUBJECT = "Email Reset Request";

    public void execute(Long id, String newEmail) {
        log.info("Requesting email reset for user with id: [{}]", id);

        User user = findUserByIdUseCase.execute(id);

        User isUserDuplicated = findUserByEmailBoundary.execute(newEmail);

        if (nonNull(isUserDuplicated)) {
            throw new ResourceConflictException("User email already exists");
        }

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

        Map<String, String> notificationBodyVariables = new HashMap<>();
        notificationBodyVariables.put("userId", user.getId().toString());
        notificationBodyVariables.put("emailResetHash", emailResetHash);

        sendEmailNotificationUseCase.execute(user.getId(), user.getNewEmailReset(), NotificationIdsEnum.EMAIL_RESET_REQUEST.getNotificationId(),
                NOTIFICATION_SUBJECT, NotificationBodyEnum.EMAIL_RESET_REQUEST, notificationBodyVariables);
    }
}
