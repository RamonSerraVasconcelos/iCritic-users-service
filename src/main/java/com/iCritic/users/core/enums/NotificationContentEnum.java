package com.iCritic.users.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NotificationContentEnum {

    EMAIL_RESET_REQUEST("email-reset-request", "Email Reset Request", "<h1>Email reset request</h1> <br> <p>If you didn't request an email reset, ignore this email.</p> <a href=\\${front_end_link}/{userId}/{emailResetHash}\"\\\" target=\\\"_blank\\\">Click here</a> to reset your email!"),
    EMAIL_RESET("email-reset", "Email Reset Notification", "<h1>Email reset notification</h1> <br> <h2>Your email has been reset, if you did do not recognize this operation, please contact us.</h2>"),
    PASSWORD_RESET_REQUEST("password-reset-request", "Password Reset Request", "<h1>Password reset request</h1> <br> <p>If you didn't request a password reset, ignore this email.</p> <br> <a href=\"{frontEndLink}/{passwordResetHash}\" target=\"_blank\">Click here</a> to reset your password!"),
    PASSWORD_RESET("password-reset", "Password reset notification", "<p>Your password was successfully reset on iCritic.</p>"),
    PASSWORD_CHANGE("password-change", "Password Reset Request", "<h1>Password change notification</h1> <br> <p>Your password was changed. If you didnt request this change please contact us.</p>");

    private final String notificationId;
    private final String subject;
    private final String bodyTemplate;
}
