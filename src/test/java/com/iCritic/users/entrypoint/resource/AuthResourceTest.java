package com.iCritic.users.entrypoint.resource;

import com.iCritic.users.core.fixture.CountryFixture;
import com.iCritic.users.core.fixture.UserFixture;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.CreateUserUseCase;
import com.iCritic.users.core.usecase.SignInUserUseCase;
import com.iCritic.users.entrypoint.fixture.AuthorizationDataFixture;
import com.iCritic.users.entrypoint.fixture.UserRequestDtoFixture;
import com.iCritic.users.entrypoint.fixture.UserResponseDtoFixture;
import com.iCritic.users.entrypoint.mapper.UserDtoMapper;
import com.iCritic.users.entrypoint.model.AuthorizationData;
import com.iCritic.users.entrypoint.model.UserRequestDto;
import com.iCritic.users.entrypoint.model.UserResponseDto;
import com.iCritic.users.dataprovider.gateway.jwt.JwtProvider;
import com.iCritic.users.exception.ResourceViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletResponse;
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

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    HttpServletResponse response;

    @Test
    void givenCallToRegisterUserWithValidParameters_callCreateUserUseCase_thenReturnUserResponseDto() {
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
        when(signInUserUseCase.execute(any())).thenReturn(user);
        when(jwtProvider.generateToken(any())).thenReturn(authorizationData.getAccessToken());
        when(jwtProvider.generateRefreshToken(any())).thenReturn(authorizationData.getRefreshToken());

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
        User user = UserFixture.load();

        Set<ConstraintViolation<UserRequestDto>> violations = new HashSet<>();
        violations.add(createMockViolation("name"));
        violations.add(createMockViolation("countryId"));

        when(validator.validate(userRequestDto)).thenReturn(violations);
        when(signInUserUseCase.execute(any())).thenReturn(user);

        assertDoesNotThrow(() -> authResource.loginUser(userRequestDto, response));
    }

    private <T> ConstraintViolation<T> createMockViolation(String propertyName) {
        ConstraintViolation<T> violation = Mockito.mock(ConstraintViolation.class);
        Path propertyPath = Mockito.mock(Path.class);
        Mockito.when(propertyPath.toString()).thenReturn(propertyName);
        Mockito.when(violation.getPropertyPath()).thenReturn(propertyPath);
        return violation;
    }
}
