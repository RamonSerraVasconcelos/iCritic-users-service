package com.iCritic.users.core.usecase.boundary;

import com.iCritic.users.core.model.EmailReset;

public interface PostEmailResetMessageBoundary {

    void execute(EmailReset emailReset);
}
