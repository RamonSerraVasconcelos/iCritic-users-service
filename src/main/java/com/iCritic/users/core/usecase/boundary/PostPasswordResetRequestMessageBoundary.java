package com.iCritic.users.core.usecase.boundary;

import com.iCritic.users.core.model.PasswordResetRequest;

public interface PostPasswordResetRequestMessageBoundary {

    public void execute(PasswordResetRequest passwordResetRequest);
}
