package com.iCritic.users.core.usecase.boundary;

import com.iCritic.users.core.model.AccessToken;

public interface GenerateAccessTokenBoundary {

    String execute(AccessToken accessToken);
}
