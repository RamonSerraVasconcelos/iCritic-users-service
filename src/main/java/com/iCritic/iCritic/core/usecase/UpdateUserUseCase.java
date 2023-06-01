package com.iCritic.iCritic.core.usecase;

import com.iCritic.iCritic.core.model.Country;
import com.iCritic.iCritic.core.model.User;
import com.iCritic.iCritic.core.usecase.boundary.FindCountryByIdBoundary;
import com.iCritic.iCritic.core.usecase.boundary.FindUserByIdBoundary;
import com.iCritic.iCritic.core.usecase.boundary.UpdateUserBoundary;
import com.iCritic.iCritic.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateUserUseCase {

    private final UpdateUserBoundary updateUserBoundary;

    private final FindUserByIdBoundary findUserByIdBoundary;

    private final FindCountryByIdBoundary findCountryByIdBoundary;

    public User execute(Long userId, User user) {
        User userToBeUpdated = findUserByIdBoundary.execute(userId);

        Country country = findCountryByIdBoundary.execute(user.getCountryId());

        User userToUpdate = User.builder()
                .id(userId)
                .name(user.getName())
                .email(userToBeUpdated.getEmail())
                .password(userToBeUpdated.getPassword())
                .description(user.getDescription())
                .country(country)
                .active(userToBeUpdated.isActive())
                .role(userToBeUpdated.getRole())
                .createdAt(userToBeUpdated.getCreatedAt())
                .build();

        return updateUserBoundary.execute(userToUpdate);
    }
}
