package com.iCritic.users.entrypoint.resource;

import com.iCritic.users.core.fixture.CountryFixture;
import com.iCritic.users.core.fixture.UserFixture;
import com.iCritic.users.core.model.AuthorizationData;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.CreateUserUseCase;
import com.iCritic.users.core.usecase.PasswordResetRequestUseCase;
import com.iCritic.users.core.usecase.PasswordResetUseCase;
import com.iCritic.users.core.usecase.RefreshUserTokenUseCase;
import com.iCritic.users.core.usecase.SignInUserUseCase;
import com.iCritic.users.entrypoint.fixture.AuthorizationDataFixture;
import com.iCritic.users.entrypoint.fixture.PasswordResetDataFixture;
import com.iCritic.users.entrypoint.fixture.UserRequestDtoFixture;
import com.iCritic.users.entrypoint.fixture.UserResponseDtoFixture;
import com.iCritic.users.entrypoint.mapper.UserDtoMapper;
import com.iCritic.users.entrypoint.model.PasswordResetData;
import com.iCritic.users.entrypoint.model.UserRequestDto;
import com.iCritic.users.entrypoint.model.UserResponseDto;
import com.iCritic.users.exception.ResourceViolationException;
import com.iCritic.users.exception.UnauthorizedAccessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
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
    private RefreshUserTokenUseCase refreshUserTokenUseCase;

    @Mock
    private PasswordResetRequestUseCase passwordResetRequestUseCase;

    @Mock
    private PasswordResetUseCase passwordResetUseCase;

    @Mock
    private Validator validator;

    @Mock
    private UserDtoMapper userDtoMapper;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Test
    void givenCallToRegisterUserWithValidParameters_callCreateUserUseCase_thenReturnUserResponseDto() {
        UserRequestDto userRequestDto = UserRequestDtoFixture.load();
        UserResponseDto userResponseDto = UserResponseDtoFixture.load();
        User user = UserFixture.load();

        when(validator.validate(userRequestDto)).thenReturn(Collections.emptySet());

        user.setId(1L);
        user.setCountry(CountryFixture.load());
        user.setCreatedAt(LocalDateTime.now());
        user.setActive(true);

        when(createUserUseCase.execute(any())).thenReturn(user);
        when(userDtoMapper.userRequestDtoToUser(userRequestDto)).thenReturn(user);
        when(userDtoMapper.userToUserResponseDto(user)).thenReturn(userResponseDto);

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
    void givenCallToRegisterUserWithInvalidParameters_thenThrowResourceViolationException() {
        UserRequestDto userRequestDto = UserRequestDtoFixture.load();

        Set<ConstraintViolation<UserRequestDto>> violations = new HashSet<>();
        violations.add(createMockViolation("email"));
        when(validator.validate(userRequestDto)).thenReturn(violations);

        assertThrows(ResourceViolationException.class, () -> authResource.registerUser(userRequestDto));
    }

    @Test
    void givenCallToSignInUserWithValidParameters_callSignInUserUseCase_thenReturnAuthorizationData() {
        UserRequestDto userRequestDto = UserRequestDtoFixture.load();
        AuthorizationData authorizationData = AuthorizationDataFixture.load();
        User user = UserFixture.load();

        when(validator.validate(userRequestDto)).thenReturn(Collections.emptySet());
        when(signInUserUseCase.execute(any())).thenReturn(authorizationData);

        AuthorizationData returnedAuthData = authResource.loginUser(userRequestDto, response);

        verify(validator).validate(userRequestDto);
        verify(signInUserUseCase).execute(any());

        assertNotNull(returnedAuthData);
        assertEquals(returnedAuthData.getAccessToken(), authorizationData.getAccessToken());
    }

    @Test
    void givenCallToSignInUserWithInvalidParameters_thenThrowResourceViolationException() {
        UserRequestDto userRequestDto = UserRequestDtoFixture.load();

        Set<ConstraintViolation<UserRequestDto>> violations = new HashSet<>();
        violations.add(createMockViolation("email"));
        when(validator.validate(userRequestDto)).thenReturn(violations);

        assertThrows(ResourceViolationException.class, () -> authResource.loginUser(userRequestDto, response));
    }

    @Test
    void givenCallToSignInUserWithInvalidParameters_whenParametersAreNotEmailOrPassword_thenDontThrowException() {
        UserRequestDto userRequestDto = UserRequestDtoFixture.load();
        AuthorizationData authorizationData = AuthorizationDataFixture.load();

        Set<ConstraintViolation<UserRequestDto>> violations = new HashSet<>();
        violations.add(createMockViolation("name"));
        violations.add(createMockViolation("countryId"));

        when(validator.validate(userRequestDto)).thenReturn(violations);
        when(signInUserUseCase.execute(any())).thenReturn(authorizationData);

        assertDoesNotThrow(() -> authResource.loginUser(userRequestDto, response));
    }

    @Test
    void givenCallToRefreshTokenWithValidParameters_callSignInUserUseCase_thenReturnAuthorizationData() {
        AuthorizationData authorizationData = AuthorizationDataFixture.load();

        when(refreshUserTokenUseCase.execute(authorizationData.getRefreshToken())).thenReturn(authorizationData);

        AuthorizationData returnedAuthData = authResource.refreshToken(authorizationData, request, response);

        verify(refreshUserTokenUseCase).execute(authorizationData.getRefreshToken());

        assertNotNull(returnedAuthData);
        assertEquals(returnedAuthData.getAccessToken(), authorizationData.getAccessToken());
    }

    @Test
    void givenCallToRefreshTokenWithNullRefreshToken_thenThrowUnauthorizedAccessException() {
        AuthorizationData authorizationData = AuthorizationDataFixture.load();
        authorizationData.setRefreshToken(null);

        assertThrows(UnauthorizedAccessException.class, () -> authResource.refreshToken(authorizationData, request, response));
    }

    @Test
    void givenCallToPasswordResetRequestWithValidParameters_thenReturnStatusOk() {
        UserRequestDto userRequestDto = UserRequestDtoFixture.load();

        ResponseEntity<Void> response = authResource.passwordResetRequest(userRequestDto);

        verify(passwordResetRequestUseCase).execute(userRequestDto.getEmail());
        assertEquals(response.getStatusCode().value(), HttpServletResponse.SC_OK);
    }

    @Test
    void givenCallToPasswordResetRequestWithInvalidParameters_thenThrowResourceViolationException() {
        UserRequestDto userRequestDto = UserRequestDtoFixture.load();
        userRequestDto.setEmail(null);

        assertThrows(ResourceViolationException.class, () -> authResource.passwordResetRequest(userRequestDto));

        verifyNoInteractions(passwordResetRequestUseCase);
    }

    @Test
    void givenCallToPasswordResetWithValidParameters_thenReturnStatusOk() {
        PasswordResetData passwordResetData = PasswordResetDataFixture.load();

        when(validator.validate(passwordResetData)).thenReturn(Collections.emptySet());

        ResponseEntity<Void> response = authResource.passwordReset(passwordResetData);

        verify(passwordResetUseCase).execute(passwordResetData.getEmail(), passwordResetData.getPasswordResetHash(), passwordResetData.getPassword());
        assertEquals(response.getStatusCode().value(), HttpServletResponse.SC_OK);
    }

    @Test
    void givenCallToPasswordResetWithInvalidParameters_thenThrowResourceViolationException() {
        PasswordResetData passwordResetData = PasswordResetDataFixture.load();

        Set<ConstraintViolation<PasswordResetData>> violations = new HashSet<>();
        violations.add(createMockViolation("name"));

        when(validator.validate(passwordResetData)).thenReturn(violations);

        assertThrows(ResourceViolationException.class, () -> authResource.passwordReset(passwordResetData));
        verifyNoInteractions(passwordResetUseCase);
    }

    private <T> ConstraintViolation<T> createMockViolation(String propertyName) {
        ConstraintViolation<T> violation = Mockito.mock(ConstraintViolation.class);
        Path propertyPath = Mockito.mock(Path.class);
        Mockito.when(propertyPath.toString()).thenReturn(propertyName);
        Mockito.when(violation.getPropertyPath()).thenReturn(propertyPath);
        return violation;
    }
}
