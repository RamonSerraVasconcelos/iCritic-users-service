package com.iCritic.users.entrypoint.resource;

import com.iCritic.users.core.model.AuthorizationData;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.CreateUserUseCase;
import com.iCritic.users.core.usecase.DecryptAccessTokenUseCase;
import com.iCritic.users.core.usecase.EmailResetUseCase;
import com.iCritic.users.core.usecase.PasswordResetRequestUseCase;
import com.iCritic.users.core.usecase.PasswordResetUseCase;
import com.iCritic.users.core.usecase.RefreshUserTokenUseCase;
import com.iCritic.users.core.usecase.SignInUserUseCase;
import com.iCritic.users.entrypoint.entity.AccessTokenDecryptedResponseDto;
import com.iCritic.users.entrypoint.entity.EmailResetData;
import com.iCritic.users.entrypoint.mapper.UserDtoMapper;
import com.iCritic.users.entrypoint.entity.PasswordResetData;
import com.iCritic.users.entrypoint.entity.UserRequestDto;
import com.iCritic.users.entrypoint.entity.UserResponseDto;
import com.iCritic.users.exception.ResourceViolationException;
import com.iCritic.users.exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static java.util.Objects.isNull;

@RestController
@RequiredArgsConstructor
public class AuthResource {

    private final CreateUserUseCase createUserUseCase;

    private final SignInUserUseCase signInUserUseCase;

    private final PasswordResetRequestUseCase passwordResetRequestUseCase;

    private final PasswordResetUseCase passwordResetUseCase;

    private final RefreshUserTokenUseCase refreshUserTokenUseCase;

    private final EmailResetUseCase emailResetUseCase;

    private final DecryptAccessTokenUseCase decryptAccessTokenUseCase;

    private final Validator validator;

    private final UserDtoMapper userDtoMapper;

    @PostMapping(path = "/register")
    public UserResponseDto registerUser(@RequestBody UserRequestDto userRequestDto) {
        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(userRequestDto);
        if (!violations.isEmpty()) {
            throw new ResourceViolationException(violations);
        }

        User user = userDtoMapper.userRequestDtoToUser(userRequestDto);

        User createdUser = createUserUseCase.execute(user);

        return userDtoMapper.userToUserResponseDto(createdUser);
    }

    @PostMapping(path = "/login")
    public AuthorizationData loginUser(@RequestBody UserRequestDto userRequestDto, HttpServletResponse response) {

        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(userRequestDto);
        if (!violations.isEmpty()) {
            violations.forEach(violation -> {
                if (violation.getPropertyPath().toString().equals("email") || violation.getPropertyPath().toString().equals("password")) {
                    throw new ResourceViolationException(violation);
                }
            });
        }

        User user = userDtoMapper.userRequestDtoToUser(userRequestDto);

        AuthorizationData authorizationData = signInUserUseCase.execute(user);

        Cookie refreshTokenCookie = new Cookie("refreshToken", authorizationData.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);

        response.addCookie(refreshTokenCookie);

        return authorizationData;
    }

    @PostMapping(path = "/refresh")
    public AuthorizationData refreshToken(@RequestBody AuthorizationData authorizationDataRequest, HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractRefreshTokenFromRequest(request, authorizationDataRequest);

        if (isNull(refreshToken)) {
            throw new UnauthorizedAccessException("Invalid refresh token");
        }

        AuthorizationData authorizationData = refreshUserTokenUseCase.execute(refreshToken);

        Cookie refreshTokenCookie = new Cookie("refreshToken", authorizationData.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);

        response.addCookie(refreshTokenCookie);

        return authorizationData;
    }

    @PostMapping(path = "/forgot-password")
    public ResponseEntity<Void> passwordResetRequest(@RequestBody UserRequestDto userRequestDto) {
        if (isNull(userRequestDto.getEmail())) {
            throw new ResourceViolationException("Email is required");
        }

        passwordResetRequestUseCase.execute(userRequestDto.getEmail());

        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/reset-password")
    public ResponseEntity<Void> passwordReset(@RequestBody PasswordResetData passwordResetData) {
        Set<ConstraintViolation<PasswordResetData>> violations = validator.validate(passwordResetData);
        if (!violations.isEmpty()) {
            throw new ResourceViolationException(violations);
        }

        passwordResetUseCase.execute(passwordResetData.getEmail(), passwordResetData.getPasswordResetHash(), passwordResetData.getPassword());

        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/reset-email")
    public ResponseEntity<Void> emailReset(@RequestBody EmailResetData emailResetData) {
        Set<ConstraintViolation<EmailResetData>> violations = validator.validate(emailResetData);
        if (!violations.isEmpty()) {
            throw new ResourceViolationException(violations);
        }

        emailResetUseCase.execute(emailResetData.getUserId(), emailResetData.getEmailResetHash());

        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/auth/token/validate")
    public ResponseEntity<AccessTokenDecryptedResponseDto> decryptAccessToken(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization").replace("Bearer ", "");

        if(accessToken.isEmpty()) {
            throw new UnauthorizedAccessException("Invalid access token");
        }

        String decryptedToken = decryptAccessTokenUseCase.execute(accessToken);

        AccessTokenDecryptedResponseDto accessTokenDecryptedResponseDto = AccessTokenDecryptedResponseDto.builder()
                .decryptedToken(decryptedToken)
                .build();

        return ResponseEntity.ok(accessTokenDecryptedResponseDto);
    }

    private String extractRefreshTokenFromRequest(HttpServletRequest request, AuthorizationData authorizationDataRequest) {
        String refreshToken = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refreshToken")) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (isNull(refreshToken)) {
            refreshToken = authorizationDataRequest.getRefreshToken();
        }

        return refreshToken;
    }
}
