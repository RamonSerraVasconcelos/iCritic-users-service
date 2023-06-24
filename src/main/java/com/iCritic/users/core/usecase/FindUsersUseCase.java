package com.iCritic.users.core.usecase;

import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.FindUsersBoundary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class FindUsersUseCase {

    private final FindUsersBoundary findUsersBoundary;

    public List<User> execute() {
        return findUsersBoundary.execute();
    }
}
