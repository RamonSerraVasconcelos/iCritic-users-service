package com.iCritic.users.entrypoint.resource;

import com.iCritic.users.core.enums.BanActionEnum;
import com.iCritic.users.core.fixture.CountryFixture;
import com.iCritic.users.core.fixture.UserFixture;
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
import com.iCritic.users.entrypoint.fixture.ChangePasswordDtoFixture;
import com.iCritic.users.entrypoint.fixture.UserBanDtoFixture;
import com.iCritic.users.entrypoint.fixture.UserRequestDtoFixture;
import com.iCritic.users.entrypoint.fixture.UserResponseDtoFixture;
import com.iCritic.users.entrypoint.mapper.UserDtoMapper;
import com.iCritic.users.entrypoint.entity.UserBanDto;
import com.iCritic.users.entrypoint.entity.UserRequestDto;
import com.iCritic.users.entrypoint.entity.UserResponseDto;
import com.iCritic.users.exception.ResourceViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserResourceTest {

    @InjectMocks
    private UserResource userResource;

    @InjectMocks
    MockHttpServletRequest request;

    @Mock
    private FindUsersUseCase findUsersUseCase;

    @Mock
    private FindUserByIdUseCase findUserByIdUseCase;

    @Mock
    private UpdateUserUseCase updateUserUseCase;

    @Mock
    private UpdateUserRoleUseCase updateUserRoleUseCase;

    @Mock
    private UpdateUserStatusUseCase updateUserStatusUseCase;

    @Mock
    private Validator validator;

    @Mock
    private ValidateUserRoleUseCase validateUserRoleUseCase;

    @Mock
    private UpdateUserPictureUseCase updateUserPictureUseCase;

    @Mock
    private EmailResetRequestUseCase emailResetRequestUseCase;

    @Mock
    private PasswordChangeUseCase passwordChangeUseCase;

    @Mock
    private UserDtoMapper userDtoMapper;

    @Captor
    private ArgumentCaptor<BanActionEnum> actionCaptor;

    @Test
    void givenCallToLoadAllUsers_thenCallFindUsersUseCaseAndReturnUsers() {
        List<User> users = List.of(UserFixture.load(), UserFixture.load(), UserFixture.load());
        UserResponseDto userResponseDto = UserResponseDtoFixture.load();

        when(findUsersUseCase.execute()).thenReturn(users);
        when(userDtoMapper.userToUserResponseDto(any())).thenReturn(userResponseDto);

        List<UserResponseDto> returnedUsers = userResource.loadAll();

        assertNotNull(returnedUsers);
        assertEquals(returnedUsers.get(0).getName(), users.get(0).getName());
        assertEquals(returnedUsers.get(0).getEmail(), users.get(0).getEmail());
        assertEquals(returnedUsers.get(0).getDescription(), users.get(0).getDescription());
        assertEquals(returnedUsers.get(0).isActive(), users.get(0).isActive());
        assertEquals(returnedUsers.get(0).getRole(), users.get(0).getRole());
        assertEquals(returnedUsers.get(0).getCountry().getId(), users.get(0).getCountry().getId());
        assertEquals(returnedUsers.get(0).getCountry().getName(), users.get(0).getCountry().getName());
        assertNotNull(returnedUsers.get(0).getCreatedAt());
    }

    @Test
    void givenCallToGetUserWithValidParameters_thenCallFindUserByIdUseCaseAndReturnUser() {
        User user = UserFixture.load();
        UserResponseDto userResponseDto = UserResponseDtoFixture.load();

        when(findUserByIdUseCase.execute(user.getId())).thenReturn(user);
        when(userDtoMapper.userToUserResponseDto(any())).thenReturn(userResponseDto);

        UserResponseDto returnedUser = userResource.get(user.getId());

        verify(findUserByIdUseCase).execute(user.getId());
        assertNotNull(returnedUser);
        assertEquals(returnedUser.getName(), user.getName());
        assertEquals(returnedUser.getEmail(), user.getEmail());
        assertEquals(returnedUser.getDescription(), user.getDescription());
        assertEquals(returnedUser.isActive(), user.isActive());
        assertEquals(returnedUser.getRole(), user.getRole());
        assertEquals(returnedUser.getCountry().getId(), user.getCountry().getId());
        assertEquals(returnedUser.getCountry().getName(), user.getCountry().getName());
        assertNotNull(returnedUser.getCreatedAt());
    }

    @Test
    void givenCallToUpdateUserWithValidParameters_thenCallUpdateUserUseCaseAndReturnUser() {
        UserRequestDto userRequestDto = UserRequestDtoFixture.load();
        UserResponseDto userResponseDto = UserResponseDtoFixture.load();
        User user = UserFixture.load();

        when(validator.validate(userRequestDto)).thenReturn(Collections.emptySet());
        when(userDtoMapper.userRequestDtoToUser(userRequestDto)).thenReturn(user);
        when(userDtoMapper.userToUserResponseDto(user)).thenReturn(userResponseDto);

        user.setId(1L);
        user.setCountry(CountryFixture.load());
        user.setCreatedAt(LocalDateTime.now());
        user.setActive(true);

        when(updateUserUseCase.execute(any(), any())).thenReturn(user);

        request.setAttribute("userId", user.getId());

        UserResponseDto updatedUser = userResource.update(request, userRequestDto);

        verify(validator).validate(userRequestDto);
        verify(updateUserUseCase).execute(any(), any());

        assertNotNull(updatedUser);
        assertNotNull(updatedUser.getId());
        assertEquals(updatedUser.getName(), user.getName());
        assertEquals(updatedUser.getEmail(), user.getEmail());
        assertEquals(updatedUser.getDescription(), user.getDescription());
        assertEquals(updatedUser.getRole(), user.getRole());
        assertEquals(updatedUser.getCountry().getId(), user.getCountry().getId());
        assertEquals(updatedUser.getCountry().getName(), user.getCountry().getName());
        assertNotNull(updatedUser.getCreatedAt());
        assertTrue(updatedUser.isActive());
    }

    @Test
    void givenCallToUpdateUserWithInvalidParameters_thenThrowResourceViolationException() {
        UserRequestDto userRequestDto = UserRequestDtoFixture.load();

        Set<ConstraintViolation<UserRequestDto>> violations = new HashSet<>();
        violations.add(createMockViolation("name"));

        when(validator.validate(userRequestDto)).thenReturn(violations);

        assertThrows(ResourceViolationException.class, () -> userResource.update(request, userRequestDto));
    }

    @Test
    void givenCallToUpdateUserWithInvalidParameters_whenParametersAreEmailOrPassword_thenDontThrowException() {
        UserRequestDto userRequestDto = UserRequestDtoFixture.load();
        User user = UserFixture.load();
        user.setId(1L);

        Set<ConstraintViolation<UserRequestDto>> violations = new HashSet<>();
        violations.add(createMockViolation("email"));
        violations.add(createMockViolation("password"));

        request.setAttribute("userId", user.getId());

        when(validator.validate(userRequestDto)).thenReturn(violations);
        when(updateUserUseCase.execute(any(), any())).thenReturn(user);

        assertDoesNotThrow(() -> userResource.update(request, userRequestDto));
    }

    @Test
    void givenCallToChangeProfilePictureWithValidParameters_thenCallupdateUserPictureUseCaseAndReturnOk() {
        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png", "test.png".getBytes());

        request.setAttribute("userId", 1L);
        doNothing().when(updateUserPictureUseCase).execute(any(), any(), any());

        ResponseEntity<Void> response = userResource.changeProfilePicture(request, file);

        verify(updateUserPictureUseCase).execute(any(), any(), any());
        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void givenCallToChangeRoleWithValidParameters_thenCallUpdateUserRoleUseCase() {
        UserRequestDto userRequestDto = UserRequestDtoFixture.load();

        request.setAttribute("role", userRequestDto.getRole());

        doNothing().when(validateUserRoleUseCase).execute(any(), any());
        doNothing().when(updateUserRoleUseCase).execute(any(), any());

        userResource.changeRole(request, userRequestDto.getId(), userRequestDto);

        verify(validateUserRoleUseCase).execute(any(), any());
        verify(updateUserRoleUseCase).execute(any(), any());
    }

    @Test
    void givenCallToBanUserWithValidParameters_thenCallUpdateUserStatusUseCaseWithActionBan_andReturnResponseOk() {
        Long id = 1L;
        UserBanDto userBanDto = UserBanDtoFixture.load();

        request.setAttribute("role", "ADMIN");

        when(validator.validate(userBanDto)).thenReturn(Collections.emptySet());
        doNothing().when(updateUserStatusUseCase).execute(any(), any(), actionCaptor.capture());

        ResponseEntity<Void> response = userResource.ban(request, id, userBanDto);

        assertEquals(actionCaptor.getValue(), BanActionEnum.BAN);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void givenCallToBanUserWithInvalidParameters_thenThrowResourceViolationException() {
        Long id = 1L;
        UserBanDto userBanDto = UserBanDtoFixture.load();

        request.setAttribute("role", "ADMIN");

        Set<ConstraintViolation<UserBanDto>> violations = new HashSet<>();
        violations.add(createMockViolation("motive"));

        when(validator.validate(userBanDto)).thenReturn(violations);

        assertThrows(ResourceViolationException.class, () -> userResource.ban(request, id, userBanDto));
    }

    @Test
    void givenCallToUnbanUserWithValidParameters_thenCallUpdateUserStatusUseCaseWithActionUnban_andReturnResponseOk() {
        Long id = 1L;
        UserBanDto userBanDto = UserBanDtoFixture.load();

        request.setAttribute("role", "ADMIN");

        when(validator.validate(userBanDto)).thenReturn(Collections.emptySet());
        doNothing().when(updateUserStatusUseCase).execute(any(), any(), actionCaptor.capture());

        ResponseEntity<Void> response = userResource.unban(request, id, userBanDto);

        assertEquals(actionCaptor.getValue(), BanActionEnum.UNBAN);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void givenCallToUnbanUserWithInvalidParameters_thenThrowResourceViolationException() {
        Long id = 1L;
        UserBanDto userBanDto = UserBanDtoFixture.load();

        request.setAttribute("role", "ADMIN");

        Set<ConstraintViolation<UserBanDto>> violations = new HashSet<>();
        violations.add(createMockViolation("motive"));

        when(validator.validate(userBanDto)).thenReturn(violations);

        assertThrows(ResourceViolationException.class, () -> userResource.unban(request, id, userBanDto));
    }

    @Test
    void givenCallToEmailResetRequestWithValidParameters_thenCallEmailResetRequestUseCase() {
        UserRequestDto userRequestDto = UserRequestDtoFixture.load();

        request.setAttribute("userId", "1");

        ResponseEntity<Void> response = userResource.requestEmailChange(request, userRequestDto);

        verify(emailResetRequestUseCase).execute(anyLong(), anyString());
    }

    @Test
    void givenCallToEmailResetRequestWithInvalidParameters_thenThrowResourceViolationException() {
        UserRequestDto userRequestDto = UserRequestDtoFixture.load();
        userRequestDto.setEmail(null);

        request.setAttribute("userId", "1");

        assertThrows(ResourceViolationException.class, () -> userResource.requestEmailChange(request, userRequestDto));
    }

    @Test
    void givenCallToChangePasswordWithValidParameters_thenCallUseCaseAndReturnOk() {
        ChangePasswordDto changePasswordDto = ChangePasswordDtoFixture.load();

        request.setAttribute("userId", "1");

        ResponseEntity<Void> response = userResource.changePassword(request, changePasswordDto);

        verify(passwordChangeUseCase).execute(any(), any(), any(), any());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void givenCallToChangePasswordWithInvalidParameters_thenThrowResourceViolationException() {
        ChangePasswordDto changePasswordDto = ChangePasswordDtoFixture.load();
        changePasswordDto.setPassword(null);

        request.setAttribute("userId", "1");

        Set<ConstraintViolation<ChangePasswordDto>> violations = new HashSet<>();
        violations.add(createMockViolation("passowrd"));

        when(validator.validate(changePasswordDto)).thenReturn(violations);

        assertThrows(ResourceViolationException.class, () -> userResource.changePassword(request, changePasswordDto));
    }

    private <T> ConstraintViolation<T> createMockViolation(String propertyName) {
        ConstraintViolation<T> violation = Mockito.mock(ConstraintViolation.class);
        Path propertyPath = Mockito.mock(Path.class);
        Mockito.when(propertyPath.toString()).thenReturn(propertyName);
        Mockito.when(violation.getPropertyPath()).thenReturn(propertyPath);
        return violation;
    }
}
