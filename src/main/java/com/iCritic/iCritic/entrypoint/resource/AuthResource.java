package com.iCritic.iCritic.entrypoint.resource;

import com.iCritic.iCritic.core.model.User;
import com.iCritic.iCritic.core.usecase.CreateUserUseCase;
import com.iCritic.iCritic.core.usecase.SignInUserUseCase;
import com.iCritic.iCritic.entrypoint.mapper.UserDtoMapper;
import com.iCritic.iCritic.entrypoint.model.AuthorizationData;
import com.iCritic.iCritic.entrypoint.model.UserRequestDto;
import com.iCritic.iCritic.entrypoint.model.UserResponseDto;
import com.iCritic.iCritic.exception.ResourceViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class AuthResource {

    private final CreateUserUseCase createUserUseCase;

    private final SignInUserUseCase signInUserUseCase;

    private final Validator validator;

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
    public AuthorizationData loginUser(@RequestBody UserRequestDto userRequestDto) {

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

        return signInUserUseCase.execute(user);
    }
}
