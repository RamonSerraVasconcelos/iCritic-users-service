package com.iCritic.users.core.usecase;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.iCritic.users.core.fixture.UserFixture;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.FindUserByIdBoundary;
import com.iCritic.users.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindUserByIdUseCaseTest {

    @InjectMocks
    private FindUserByIdUseCase findUserByIdUseCase;

    @Mock
    private FindUserByIdBoundary findUserByIdBoundary;

    @Test
    void givenValidId_thenCallGatewayAndReturnUser() {
        User user = UserFixture.load();

        when(findUserByIdBoundary.execute(user.getId())).thenReturn(user);

        User returnedUser = findUserByIdUseCase.execute(user.getId());

        verify(findUserByIdBoundary).execute(user.getId());
        assertNotNull(returnedUser);
        assertEquals(returnedUser.getName(), user.getName());
        assertEquals(returnedUser.getEmail(), user.getEmail());
        assertEquals(returnedUser.getDescription(), user.getDescription());
        assertEquals(returnedUser.isActive(), user.isActive());
        assertEquals(returnedUser.getRole(), user.getRole());
        assertEquals(returnedUser.getCountry(), user.getCountry());
        assertEquals(returnedUser.getCreatedAt(), user.getCreatedAt());
    }

    @Test
    void givenInvalidId_whenResourceNotFoundExceptionIsThrow_thenLogErrorAndRethrowException() {
        doThrow(ResourceNotFoundException.class).when(findUserByIdBoundary).execute(1L);

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        loggerContext.getLogger("com.iCritic.users.core.usecase.FindUserByIdUseCase").addAppender(listAppender);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> findUserByIdUseCase.execute(1L));

        List<ILoggingEvent> loggingEvents = listAppender.list;
        String loggedError = loggingEvents.toArray()[0].toString();
        assertEquals("[ERROR] User with id: [1] not found", loggedError);
    }
}
