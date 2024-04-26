package com.iCritic.users.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NotificationIdsEnum {

    EMAIL_RESET_REQUEST("email-reset-request"),
    EMAIL_RESET("email-reset"),
    PASSWORD_RESET_REQUEST("password-reset-request"),
    PASSWORD_RESET("password-reset"),
    PASSWORD_CHANGE("password-change");

    private final String notificationId;
}
