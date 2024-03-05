package com.iCritic.users.core.usecase;

import com.iCritic.users.core.fixture.UserFixture;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.FindUsersBoundary;
import com.iCritic.users.core.usecase.boundary.FindUsersCachedBoundary;
import com.iCritic.users.core.usecase.boundary.SaveUsersToCacheBoundary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindUsersUseCaseTest {

    @InjectMocks
    private FindUsersUseCase findUsersUseCase;

    @Mock
    private FindUsersBoundary findUsersBoundary;

    @Mock
    private FindUsersCachedBoundary findUsersCachedBoundary;

    @Mock
    private SaveUsersToCacheBoundary saveUsersToCacheBoundary;

    @Mock
    private Pageable pageable;

    @Test
    void givenCallToUseCase_thenUsersAreNotOnCache_thenReturnUsersFromDb() {
        List<User> users = List.of(
                UserFixture.load(),
                UserFixture.load(),
                UserFixture.load()
        );


        Page<User> userPage = new PageImpl<>(users, pageable, 3);
        when(findUsersBoundary.execute(pageable)).thenReturn(userPage);

        Page<User> returnedUsers = findUsersUseCase.execute(pageable);

        verify(findUsersBoundary).execute(pageable);
        verify(saveUsersToCacheBoundary).execute(userPage);

        assertThat(returnedUsers).isNotNull().isNotEmpty();
        assertThat(returnedUsers.getTotalElements()).isEqualTo(3);
        assertThat(returnedUsers.getContent().get(0)).usingRecursiveComparison().isEqualTo(users.get(0));
    }

    @Test
    void givenCallToUseCase_thenUsersAreOnCache_thenReturnUsersFromCache() {
        List<User> users = List.of(
                UserFixture.load(),
                UserFixture.load(),
                UserFixture.load()
        );

        Page<User> userPage = new PageImpl<>(users, pageable, 3);
        when(findUsersCachedBoundary.execute(pageable)).thenReturn(userPage);

        Page<User> returnedUsers = findUsersUseCase.execute(pageable);

        verify(findUsersCachedBoundary).execute(pageable);
        verifyNoInteractions(findUsersBoundary);
        verifyNoInteractions(saveUsersToCacheBoundary);

        assertThat(returnedUsers).isNotNull().isNotEmpty();
        assertThat(returnedUsers.getTotalElements()).isEqualTo(3);
    }
}
