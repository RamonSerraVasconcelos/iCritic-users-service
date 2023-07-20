package com.iCritic.users.entrypoint.resource;

import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.CreateUserUseCase;
import com.iCritic.users.core.usecase.FindUserByIdUseCase;
import com.iCritic.users.core.usecase.SignInUserUseCase;
import com.iCritic.users.dataprovider.jwt.JwtProvider;
import com.iCritic.users.entrypoint.mapper.UserDtoMapper;
import com.iCritic.users.entrypoint.model.AuthorizationData;
import com.iCritic.users.entrypoint.model.UserRequestDto;
import com.iCritic.users.entrypoint.model.UserResponseDto;
import com.iCritic.users.exception.ResourceViolationException;
import com.iCritic.users.exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
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
import static java.util.Objects.nonNull;

@RestController
@RequiredArgsConstructor
public class AuthResource {

    private final CreateUserUseCase createUserUseCase;

    private final SignInUserUseCase signInUserUseCase;

    private final FindUserByIdUseCase findUserByIdUseCase;

    private final Validator validator;

    private final JwtProvider jwtProvider;

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

        User loggedUser = signInUserUseCase.execute(user);

        if (!nonNull(loggedUser)) {
            throw new ResourceViolationException("Invalid email or password");
        }

        AuthorizationData authorizationData = AuthorizationData.builder()
                .accessToken(jwtProvider.generateToken(loggedUser))
                .refreshToken(jwtProvider.generateRefreshToken(loggedUser))
                .build();

        Cookie refreshTokenCookie = new Cookie("refreshToken", authorizationData.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);

        response.addCookie(refreshTokenCookie);

        return authorizationData;
    }

    @PostMapping(path = "/refresh")
    public AuthorizationData refreshToken(@RequestBody AuthorizationData authorizationDataRequest, HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractRefreshTokenFromRequest(request, authorizationDataRequest);

        if(isNull(refreshToken)) {
            throw new UnauthorizedAccessException("Invalid refresh token");
        }

        if(!jwtProvider.validateRefreshToken(refreshToken)) {
            throw new UnauthorizedAccessException("Invalid refresh token");
        }

        User user = findUserByIdUseCase.execute(Long.parseLong(jwtProvider.getUserIdFromRefreshToken(refreshToken)));

        AuthorizationData authorizationData = AuthorizationData.builder()
                .accessToken(jwtProvider.generateToken(user))
                .refreshToken(jwtProvider.generateRefreshToken(user))
                .build();

        Cookie refreshTokenCookie = new Cookie("refreshToken", authorizationData.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);

        response.addCookie(refreshTokenCookie);

        return authorizationData;
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

        if(isNull(refreshToken)) {
            refreshToken = authorizationDataRequest.getRefreshToken();
        }

        return refreshToken;
    }
}
