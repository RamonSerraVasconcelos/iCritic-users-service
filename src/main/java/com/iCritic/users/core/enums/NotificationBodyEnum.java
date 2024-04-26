package com.iCritic.users.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NotificationBodyEnum {

    EMAIL_RESET_REQUEST("<h1>Email reset request</h1> <br> <p>If you didn't request an email reset, ignore this email.</p> <a href=\\${front_end_link}/{userId}/{emailResetHash}\"\\\" target=\\\"_blank\\\">Click here</a> to reset your email!"),
    EMAIL_RESET(""),
    PASSWORD_RESET_REQUEST(""),
    PASSWORD_RESET(""),
    PASSWORD_CHANGE("");


    private final String notificationBodyTemplate;
}
