package com.iCritic.users.core.usecase;

import com.iCritic.users.core.model.Country;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.FindCountryByIdBoundary;
import com.iCritic.users.core.usecase.boundary.FindUserByIdBoundary;
import com.iCritic.users.core.usecase.boundary.InvalidateUsersCacheBoundary;
import com.iCritic.users.core.usecase.boundary.UpdateUserBoundary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateUserUseCase {

    private final UpdateUserBoundary updateUserBoundary;

    private final FindUserByIdBoundary findUserByIdBoundary;

    private final FindCountryByIdBoundary findCountryByIdBoundary;

    private final InvalidateUsersCacheBoundary invalidateUsersCacheBoundary;

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

        User updatedUser = updateUserBoundary.execute(userToUpdate);

        invalidateUsersCacheBoundary.execute();

        return updatedUser;
    }
}
