package com.iCritic.iCritic.core.usecase;

import com.iCritic.iCritic.core.model.User;
import com.iCritic.iCritic.core.usecase.boundary.FindUsersBoundary;
import com.iCritic.iCritic.entrypoint.mapper.UserMapper;
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
