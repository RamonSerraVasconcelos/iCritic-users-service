package com.iCritic.iCritic.entrypoint.resource;

import com.iCritic.iCritic.core.fixture.CountryFixture;
import com.iCritic.iCritic.core.fixture.UserFixture;
import com.iCritic.iCritic.core.model.User;
import com.iCritic.iCritic.core.usecase.CreateUserUseCase;
import com.iCritic.iCritic.core.usecase.SignInUserUseCase;
import com.iCritic.iCritic.entrypoint.fixture.AuthorizationDataFixture;
import com.iCritic.iCritic.entrypoint.fixture.UserRequestDtoFixture;
import com.iCritic.iCritic.entrypoint.fixture.UserResponseDtoFixture;
import com.iCritic.iCritic.entrypoint.mapper.UserDtoMapper;
import com.iCritic.iCritic.entrypoint.model.AuthorizationData;
import com.iCritic.iCritic.entrypoint.model.UserRequestDto;
import com.iCritic.iCritic.entrypoint.model.UserResponseDto;
import com.iCritic.iCritic.exception.ResourceViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validator;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthResourceTest {

    @InjectMocks
    private AuthResource authResource;

    @Mock
    private CreateUserUseCase createUserUseCase;

    @Mock
    private SignInUserUseCase signInUserUseCase;

    @Mock
    private Validator validator;

    @Test
    void givenValidParametersOnRegisterUserCall_callCreateUserUseCase_thenReturnUserResponseDto() {
        UserRequestDto userRequestDto = UserRequestDtoFixture.load();
        UserResponseDto userResponseDto = UserResponseDtoFixture.load();
        User user = UserDtoMapper.INSTANCE.userRequestDtoToUser(userRequestDto);

        when(validator.validate(userRequestDto)).thenReturn(Collections.emptySet());

        user.setId(1L);
        user.setCountry(CountryFixture.load());
        user.setCreatedAt(LocalDateTime.now());
        user.setActive(true);

        when(createUserUseCase.execute(any())).thenReturn(user);

        UserResponseDto createdUser = authResource.registerUser(userRequestDto);

        verify(validator).validate(userRequestDto);
        verify(createUserUseCase).execute(any());

        assertNotNull(createdUser);
        assertNotNull(createdUser.getId());
        assertEquals(createdUser.getName(), userRequestDto.getName());
        assertEquals(createdUser.getEmail(), userRequestDto.getEmail());
        assertEquals(createdUser.getDescription(), userRequestDto.getDescription());
        assertEquals(createdUser.getRole(), userResponseDto.getRole());
        assertEquals(createdUser.getCountry().getId(), userResponseDto.getCountry().getId());
        assertEquals(createdUser.getCountry().getName(), userResponseDto.getCountry().getName());
        assertNotNull(createdUser.getCreatedAt());
        assertTrue(createdUser.isActive());
    }

    @Test
    void givenInvalidParametersOnRegisterUserCall_validateParemeters_thenThrowResourceViolationException() {
        UserRequestDto userRequestDto = UserRequestDtoFixture.load();

        Set<ConstraintViolation<UserRequestDto>> violations = new HashSet<>();
        violations.add(createMockViolation("email", "Invalid email format"));
        when(validator.validate(userRequestDto)).thenReturn(violations);

        assertThrows(ResourceViolationException.class, () -> authResource.registerUser(userRequestDto));
    }

    @Test
    void givenValidParametersOnSignInUserCall_callSignInUserUseCase_thenReturnAuthorizationData() {
        UserRequestDto userRequestDto = UserRequestDtoFixture.load();
        UserResponseDto userResponseDto = UserResponseDtoFixture.load();
        AuthorizationData authorizationData = AuthorizationDataFixture.load();

        when(validator.validate(userRequestDto)).thenReturn(Collections.emptySet());
        when(signInUserUseCase.execute(any())).thenReturn(authorizationData);

        AuthorizationData returnedAuthData = authResource.loginUser(userRequestDto);

        verify(validator).validate(userRequestDto);
        verify(signInUserUseCase).execute(any());

        assertNotNull(returnedAuthData);
        assertEquals(returnedAuthData.getAccessToken(), authorizationData.getAccessToken());
    }

    @Test
    void givenInvalidParametersOnSignInUserCall_validateParameters_thenThrowResourceViolationException() {
        UserRequestDto userRequestDto = UserRequestDtoFixture.load();

        Set<ConstraintViolation<UserRequestDto>> violations = new HashSet<>();
        violations.add(createMockViolation("email", "Email is required"));
        when(validator.validate(userRequestDto)).thenReturn(violations);

        assertThrows(ResourceViolationException.class, () -> authResource.loginUser(userRequestDto));
    }

    @Test
    void givenInvalidParametersOnSignInUserCall_whenParametersAreNotEmailOrPassword_thenDontThrowException() {
        UserRequestDto userRequestDto = UserRequestDtoFixture.load();
        AuthorizationData authorizationData = AuthorizationDataFixture.load();

        Set<ConstraintViolation<UserRequestDto>> violations = new HashSet<>();
        violations.add(createMockViolation("name", "Name is required"));
        violations.add(createMockViolation("countryId", "CountryId is required"));

        when(validator.validate(userRequestDto)).thenReturn(violations);
        when(signInUserUseCase.execute(any())).thenReturn(authorizationData);

        assertDoesNotThrow(() -> authResource.loginUser(userRequestDto));
    }

    private <T> ConstraintViolation<T> createMockViolation(String propertyName, String message) {
        ConstraintViolation<T> violation = Mockito.mock(ConstraintViolation.class);
        Path propertyPath = Mockito.mock(Path.class);
        Mockito.when(propertyPath.toString()).thenReturn(propertyName);
        Mockito.when(violation.getPropertyPath()).thenReturn(propertyPath);
        return violation;
    }
}