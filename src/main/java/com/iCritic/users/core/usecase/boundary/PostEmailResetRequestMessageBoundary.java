package com.iCritic.users.core.usecase.boundary;

import com.iCritic.users.core.model.EmailResetRequest;

public interface PostEmailResetRequestMessageBoundary {

    void execute(EmailResetRequest emailResetRequest);
}
