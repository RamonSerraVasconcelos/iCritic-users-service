package com.iCritic.users.core.usecase.boundary;

import com.iCritic.users.core.model.RequestPasswordResetMessage;

public interface PostRequestPasswordResetMessage {

    public void execute(RequestPasswordResetMessage requestPasswordResetMessage);
}
