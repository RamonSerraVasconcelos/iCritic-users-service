package com.iCritic.users.entrypoint.resource;

import com.iCritic.users.core.enums.BanActionEnum;
import com.iCritic.users.core.enums.Role;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.*;
import com.iCritic.users.entrypoint.mapper.UserDtoMapper;
import com.iCritic.users.entrypoint.model.UserBanDto;
import com.iCritic.users.entrypoint.model.UserRequestDto;
import com.iCritic.users.entrypoint.model.UserResponseDto;
import com.iCritic.users.core.usecase.ValidateUserRoleUseCase;
import com.iCritic.users.exception.ResourceViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserResource {

    private final FindUsersUseCase findUsersUseCase;

    private final FindUserByIdUseCase findUserByIdUseCase;

    private final UpdateUserUseCase updateUserUseCase;

    private final UpdateUserRoleUseCase updateUserRoleUseCase;

    private final UpdateUserStatusUseCase updateUserStatusUseCase;

    private final Validator validator;

    private final ValidateUserRoleUseCase validateUserRoleUseCase;

    @GetMapping
    public List<UserResponseDto> loadAll() {
        UserDtoMapper mapper = UserDtoMapper.INSTANCE;
        return findUsersUseCase.execute().stream().map(mapper::userToUserResponseDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserResponseDto get(@PathVariable Long id) {
        return UserDtoMapper.INSTANCE.userToUserResponseDto(findUserByIdUseCase.execute(id));
    }

    @PutMapping("/edit")
    public UserResponseDto update(HttpServletRequest request, @RequestBody UserRequestDto userRequestDto) {
        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(userRequestDto);
        if (!violations.isEmpty()) {
            violations.forEach(violation -> {
                if (!violation.getPropertyPath().toString().equals("email") && !violation.getPropertyPath().toString().equals("password")) {
                    throw new ResourceViolationException(violation);
                }
            });
        }

        Long userId = Long.parseLong(request.getAttribute("userId").toString());

        UserDtoMapper mapper = UserDtoMapper.INSTANCE;

        User user = mapper.userRequestDtoToUser(userRequestDto);

        User updatedUser = updateUserUseCase.execute(userId, user);
        return mapper.userToUserResponseDto(updatedUser);
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<Void> changeRole(HttpServletRequest request, @PathVariable Long id, @RequestBody UserRequestDto userDto) {
        String role = request.getAttribute("role").toString();
        validateUserRoleUseCase.execute(List.of(Role.MODERATOR), role);

        updateUserRoleUseCase.execute(id, userDto.getRole());

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/ban")
    public ResponseEntity<Void> ban(HttpServletRequest request, @PathVariable Long id, @RequestBody UserBanDto banDto) {
        String role = request.getAttribute("role").toString();
        validateUserRoleUseCase.execute(List.of(Role.MODERATOR), role);

        Set<ConstraintViolation<UserBanDto>> violations = validator.validate(banDto);
        if (!violations.isEmpty()) {
            throw new ResourceViolationException(violations);
        }

        updateUserStatusUseCase.execute(id, banDto.getMotive(), BanActionEnum.BAN);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/unban")
    public ResponseEntity<Void> unban(HttpServletRequest request, @PathVariable Long id, @RequestBody UserBanDto banDto) {
        String role = request.getAttribute("role").toString();
        validateUserRoleUseCase.execute(List.of(Role.MODERATOR), role);

        Set<ConstraintViolation<UserBanDto>> violations = validator.validate(banDto);
        if (!violations.isEmpty()) {
            throw new ResourceViolationException(violations);
        }

        updateUserStatusUseCase.execute(id, banDto.getMotive(), BanActionEnum.UNBAN);

        return ResponseEntity.ok().build();
    }
}
