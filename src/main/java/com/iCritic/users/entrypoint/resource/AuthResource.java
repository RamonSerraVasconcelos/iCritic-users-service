package com.iCritic.users.entrypoint.resource;

import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.CreateUserUseCase;
import com.iCritic.users.core.usecase.SignInUserUseCase;
import com.iCritic.users.entrypoint.mapper.UserDtoMapper;
import com.iCritic.users.entrypoint.model.AuthorizationData;
import com.iCritic.users.entrypoint.model.UserRequestDto;
import com.iCritic.users.entrypoint.model.UserResponseDto;
import com.iCritic.users.entrypoint.validation.JwtGenerator;
import com.iCritic.users.exception.ResourceViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static java.util.Objects.nonNull;

@RestController
@RequiredArgsConstructor
public class AuthResource {

    private final CreateUserUseCase createUserUseCase;

    private final SignInUserUseCase signInUserUseCase;

    private final Validator validator;

    private final JwtGenerator jwtGenerator;

    @PostMapping(path = "/register")
    public UserResponseDto registerUser(@RequestBody UserRequestDto userRequestDto) {
        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(userRequestDto);
        if (!violations.isEmpty()) {
            throw new ResourceViolationException(violations);
        }

        User user = UserDtoMapper.INSTANCE.userRequestDtoToUser(userRequestDto);

        User createdUser = createUserUseCase.execute(user);

        return UserDtoMapper.INSTANCE.userToUserResponseDto(createdUser);
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

        UserDtoMapper mapper = UserDtoMapper.INSTANCE;
        User user = mapper.userRequestDtoToUser(userRequestDto);

        User loggedUser = signInUserUseCase.execute(user);

        if(!nonNull(loggedUser)) {
            throw new ResourceViolationException("Invalid email or password");
        }

        AuthorizationData authorizationData = AuthorizationData.builder()
                .accessToken(jwtGenerator.generateToken(loggedUser))
                .refreshToken(jwtGenerator.generateRefreshToken(loggedUser))
                .build();

        Cookie refreshTokenCookie = new Cookie("refreshToken", authorizationData.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);

        response.addCookie(refreshTokenCookie);

        return authorizationData;
    }
}
