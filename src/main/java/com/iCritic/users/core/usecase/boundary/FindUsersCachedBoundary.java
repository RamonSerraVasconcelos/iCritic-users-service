package com.iCritic.users.core.usecase.boundary;

import com.iCritic.users.core.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindUsersCachedBoundary {

    Page<User> execute(Pageable pageable);
}
