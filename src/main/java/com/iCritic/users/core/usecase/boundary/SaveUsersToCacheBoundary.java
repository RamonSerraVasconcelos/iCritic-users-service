package com.iCritic.users.core.usecase.boundary;

import com.iCritic.users.core.model.User;
import org.springframework.data.domain.Page;

public interface SaveUsersToCacheBoundary {

    void execute(Page<User> users);
}
