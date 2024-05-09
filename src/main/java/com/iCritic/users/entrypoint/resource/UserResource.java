package com.iCritic.users.entrypoint.resource;

import com.iCritic.users.core.enums.BanActionEnum;
import com.iCritic.users.core.enums.Role;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.EmailResetRequestUseCase;
import com.iCritic.users.core.usecase.FindUserByIdUseCase;
import com.iCritic.users.core.usecase.FindUsersUseCase;
import com.iCritic.users.core.usecase.PasswordChangeUseCase;
import com.iCritic.users.core.usecase.UpdateUserPictureUseCase;
import com.iCritic.users.core.usecase.UpdateUserRoleUseCase;
import com.iCritic.users.core.usecase.UpdateUserStatusUseCase;
import com.iCritic.users.core.usecase.UpdateUserUseCase;
import com.iCritic.users.core.usecase.ValidateUserRoleUseCase;
import com.iCritic.users.entrypoint.entity.ChangePasswordDto;
import com.iCritic.users.entrypoint.entity.Metadata;
import com.iCritic.users.entrypoint.entity.PageableUserResponse;
import com.iCritic.users.entrypoint.entity.UserBanDto;
import com.iCritic.users.entrypoint.entity.UserRequestDto;
import com.iCritic.users.entrypoint.entity.UserResponseDto;
import com.iCritic.users.entrypoint.mapper.UserDtoMapper;
import com.iCritic.users.exception.ResourceViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

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

    private final UpdateUserPictureUseCase updateUserPictureUseCase;

    private final EmailResetRequestUseCase emailResetRequestUseCase;

    private final PasswordChangeUseCase passwordChangeUseCase;

    private final UserDtoMapper userDtoMapper;

    @GetMapping
    public ResponseEntity<PageableUserResponse> loadAll(Pageable pageable) {
        Page<User> users = findUsersUseCase.execute(pageable);

        List<UserResponseDto> userResponseDtos = users
                .stream()
                .map(userDtoMapper::userToUserResponseDto)
                .collect(Collectors.toList());

        PageableUserResponse response = PageableUserResponse.builder()
                .data(userResponseDtos)
                .metadata(Metadata.builder()
                        .page(pageable.getPageNumber())
                        .nextPage(pageable.getPageNumber() + 1)
                        .size(pageable.getPageSize())
                        .total(users.getTotalElements())
                        .build())
                .build();


        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public UserResponseDto get(@PathVariable Long id) {
        return userDtoMapper.userToUserResponseDto(findUserByIdUseCase.execute(id));
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

        User user = userDtoMapper.userRequestDtoToUser(userRequestDto);

        User updatedUser = updateUserUseCase.execute(userId, user);
        return userDtoMapper.userToUserResponseDto(updatedUser);
    }

    @PostMapping("/profile-picture")
    public ResponseEntity<Void> changeProfilePicture(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        try {
            updateUserPictureUseCase.execute(Long.parseLong(request.getAttribute("userId").toString()), file.getOriginalFilename(), file.getInputStream());
            return ResponseEntity.noContent().build();
        } catch (IOException e) {
            throw new ResourceViolationException("Invalid file");
        }
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<Void> changeRole(HttpServletRequest request, @PathVariable Long id, @RequestBody UserRequestDto userDto) {
        String role = request.getAttribute("role").toString();
        validateUserRoleUseCase.execute(List.of(Role.MODERATOR), role);

        updateUserRoleUseCase.execute(id, userDto.getRole());

        return ResponseEntity.noContent().build();
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

        return ResponseEntity.noContent().build();
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

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/request-email-change")
    public ResponseEntity<Void> requestEmailChange(HttpServletRequest request, @RequestBody UserRequestDto userDto) {
        if (isNull(userDto.getEmail())) {
            throw new ResourceViolationException("Email is required");
        }

        Long userId = Long.parseLong(request.getAttribute("userId").toString());

        emailResetRequestUseCase.execute(userId, userDto.getEmail());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(HttpServletRequest request, @RequestBody ChangePasswordDto changePasswordDto) {
        Set<ConstraintViolation<ChangePasswordDto>> violations = validator.validate(changePasswordDto);
        if (!violations.isEmpty()) {
            throw new ResourceViolationException(violations);
        }

        Long userId = Long.parseLong(request.getAttribute("userId").toString());

        passwordChangeUseCase.execute(userId, changePasswordDto.getPassword(), changePasswordDto.getNewPassword(), changePasswordDto.getConfirmPassword());

        return ResponseEntity.noContent().build();
    }
}
