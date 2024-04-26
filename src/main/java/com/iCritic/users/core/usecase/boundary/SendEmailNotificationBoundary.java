package com.iCritic.users.core.usecase.boundary;

import com.iCritic.users.core.model.EmailNotification;

public interface SendEmailNotificationBoundary {

    void execute(EmailNotification emailNotification);
}
