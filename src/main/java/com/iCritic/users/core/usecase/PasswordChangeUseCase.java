package com.iCritic.users.core.usecase;

import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.DeleteUserRefreshTokensBoundary;
import com.iCritic.users.core.usecase.boundary.FindUserByIdBoundary;
import com.iCritic.users.core.usecase.boundary.PostPasswordChangeMessageBoundary;
import com.iCritic.users.core.usecase.boundary.UpdateUserBoundary;
import com.iCritic.users.exception.ResourceViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PasswordChangeUseCase {

    private final FindUserByIdBoundary findUserByIdBoundary;

    private final UpdateUserBoundary updateUserBoundary;

    private final PostPasswordChangeMessageBoundary postPasswordChangeMessageBoundary;

    private final DeleteUserRefreshTokensBoundary deleteUserRefreshTokensBoundary;

    private final BCryptPasswordEncoder bcrypt;

    public void execute(Long userId, String currentPassword, String newPassword, String newPasswordConfirmation) {
        try {
            log.info("Changing password for user with id: [{}]", userId);

            User user = findUserByIdBoundary.execute(userId);

            boolean isCurrentPasswordValid = bcrypt.matches(currentPassword, user.getPassword());

            if(!isCurrentPasswordValid) {
                throw new ResourceViolationException("Invalid password");
            }

            boolean isPasswordDuplicated = bcrypt.matches(newPassword, user.getPassword());

            if(isPasswordDuplicated) {
                throw new ResourceViolationException("The new password cannot be equal to the old one");
            }

            if(!newPassword.equals(newPasswordConfirmation)) {
                throw new ResourceViolationException("Password confirmation does not match");
            }

            user.setPassword(bcrypt.encode(newPassword));

            updateUserBoundary.execute(user);
            postPasswordChangeMessageBoundary.execute(user.getId(), user.getEmail());
            deleteUserRefreshTokensBoundary.execute(user.getId());
        } catch (Exception e) {
            log.error("Error changing password for user with id: [{}]", userId, e);
            throw e;
        }
    }
}
