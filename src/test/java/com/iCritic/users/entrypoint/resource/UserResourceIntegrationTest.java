package com.iCritic.users.entrypoint.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.iCritic.users.entrypoint.entity.UserBanDto;
import com.iCritic.users.entrypoint.entity.UserRequestDto;
import com.iCritic.users.entrypoint.entity.UserResponseDto;
import com.iCritic.users.entrypoint.fixture.ChangePasswordDtoFixture;
import com.iCritic.users.entrypoint.fixture.UserBanDtoFixture;
import com.iCritic.users.entrypoint.fixture.UserRequestDtoFixture;
import com.iCritic.users.entrypoint.fixture.UserResponseDtoFixture;
import com.iCritic.users.entrypoint.mapper.UserDtoMapper;
import com.iCritic.users.entrypoint.validation.AuthorizationFilter;
import com.iCritic.users.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = UserResource.class)
@AutoConfigureMockMvc(addFilters = false)
class UserResourceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AuthorizationFilter authorizationFilter;

    @MockBean
    private FindUsersUseCase findUsersUseCase;

    @MockBean
    private FindUserByIdUseCase findUserByIdUseCase;

    @MockBean
    private UpdateUserUseCase updateUserUseCase;

    @MockBean
    private UpdateUserRoleUseCase updateUserRoleUseCase;

    @MockBean
    private UpdateUserStatusUseCase updateUserStatusUseCase;

    @MockBean
    private ValidateUserRoleUseCase validateUserRoleUseCase;

    @MockBean
    private UpdateUserPictureUseCase updateUserPictureUseCase;

    @MockBean
    private EmailResetRequestUseCase emailResetRequestUseCase;

    @MockBean
    private PasswordChangeUseCase passwordChangeUseCase;

    @MockBean
    private UserDtoMapper userDtoMapper;

    @Mock
    private Pageable pageable;

    @Test
    void givenRequestToUsersEndpoint_thenReturnAllUsers() throws Exception {
        List<User> users = List.of(UserFixture.load(), UserFixture.load(), UserFixture.load());
        UserResponseDto userResponseDto = UserResponseDtoFixture.load();

        Page<User> userPage = new PageImpl<>(users, pageable, 3);

        when(findUsersUseCase.execute(any(Pageable.class))).thenReturn(userPage);
        when(userDtoMapper.userToUserResponseDto(any())).thenReturn(userResponseDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(users.get(0).getId()))
                .andExpect(jsonPath("$.data[0].name").value(users.get(0).getName()))
                .andExpect(jsonPath("$.data[0].email").value(users.get(0).getEmail()))
                .andExpect(jsonPath("$.data[0].description").value(users.get(0).getDescription()))
                .andExpect(jsonPath("$.data[0].active").value(users.get(0).isActive()))
                .andExpect(jsonPath("$.data[0].role").value(users.get(0).getRole().toString()))
                .andExpect(jsonPath("$.data[0].country.id").value(users.get(0).getCountry().getId()))
                .andExpect(jsonPath("$.data[0].country.name").value(users.get(0).getCountry().getName()))
                .andExpect(jsonPath("$.data[0].createdAt").isNotEmpty());

        verify(findUsersUseCase).execute(any(Pageable.class));
    }

    @Test
    void givenRequestToGetUserEndpoint_thenReturnUser() throws Exception {
        User user = UserFixture.load();
        UserResponseDto userResponseDto = UserResponseDtoFixture.load();

        when(findUserByIdUseCase.execute(user.getId())).thenReturn(user);
        when(userDtoMapper.userToUserResponseDto(any())).thenReturn(userResponseDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.description").value(user.getDescription()))
                .andExpect(jsonPath("$.active").value(user.isActive()))
                .andExpect(jsonPath("$.role").value(user.getRole().toString()))
                .andExpect(jsonPath("$.country.id").value(user.getCountry().getId()))
                .andExpect(jsonPath("$.country.name").value(user.getCountry().getName()))
                .andExpect(jsonPath("$.createdAt").isNotEmpty());

        verify(findUserByIdUseCase).execute(user.getId());
    }

    @Test
    void givenRequestToGetUserEndpointWithInvalidId_thenReturnNotFound() throws Exception {
        Long invalidId = 0L;

        doThrow(new ResourceNotFoundException("User not found")).when(findUserByIdUseCase).execute(invalidId);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users/" + invalidId)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());

        verify(findUserByIdUseCase).execute(invalidId);
    }

    @Test
    void givenRequestToUpdateUserEndpointWithValidParameters_thenReturnUpdatedUser() throws Exception {
        UserRequestDto userRequestDto = UserRequestDtoFixture.load();
        UserResponseDto userResponseDto = UserResponseDtoFixture.load();
        User user = UserFixture.load();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(userRequestDto);

        when(updateUserUseCase.execute(anyLong(), any())).thenReturn(user);
        when(userDtoMapper.userRequestDtoToUser(userRequestDto)).thenReturn(user);
        when(userDtoMapper.userToUserResponseDto(user)).thenReturn(userResponseDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/users/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .requestAttr("userId", user.getId());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.description").value(user.getDescription()))
                .andExpect(jsonPath("$.active").value(user.isActive()))
                .andExpect(jsonPath("$.role").value(user.getRole().toString()))
                .andExpect(jsonPath("$.country.id").value(user.getCountry().getId()))
                .andExpect(jsonPath("$.country.name").value(user.getCountry().getName()))
                .andExpect(jsonPath("$.createdAt").isNotEmpty());

        verify(updateUserUseCase).execute(anyLong(), any());
    }

    @Test
    void givenRequestToUpdateUserEndpointWithInvalidParameters_thenReturnBadRequest() throws Exception {
        UserRequestDto userRequestDto = UserRequestDtoFixture.load();
        userRequestDto.setId(1L);
        userRequestDto.setName(null);
        userRequestDto.setDescription(null);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(userRequestDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/users/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .requestAttr("userId", userRequestDto.getId());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenRequestToChangeProfilePictureWithValidParameters_thenReturnResponseOk() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png", "test".getBytes());

        doNothing().when(updateUserPictureUseCase).execute(any(), any(), any());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .multipart("/users/profile-picture")
                .file(file)
                .requestAttr("userId", 1L);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    void givenRequestToChangeRoleEndpointWithValidParameters_thenReturnResponseOk() throws Exception {
        UserRequestDto userRequestDto = UserRequestDtoFixture.load();
        userRequestDto.setId(1L);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(userRequestDto);

        userRequestDto.setRole("MODERATOR");

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/users/" + userRequestDto.getId() + "/role")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .requestAttr("userId", userRequestDto.getId())
                .requestAttr("role", userRequestDto.getRole());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    void givenRequestToBanUserEndpointWithValidParameters_thenReturnResponseOk() throws Exception {
        UserBanDto userBanDto = UserBanDtoFixture.load();
        UserRequestDto userRequestDto = UserRequestDtoFixture.load();
        userRequestDto.setId(1L);
        userRequestDto.setRole("ADMIN");

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(userBanDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/users/" + userRequestDto.getId() + "/ban")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .requestAttr("userId", userRequestDto.getId())
                .requestAttr("role", userRequestDto.getRole());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());
    }

    @Test
    void givenRequestToBanUserEndpointWithInvalidParameters_thenReturnBadRequest() throws Exception {
        UserBanDto userBanDto = UserBanDtoFixture.load();
        userBanDto.setMotive(null);

        UserRequestDto userRequestDto = UserRequestDtoFixture.load();
        userRequestDto.setId(1L);
        userRequestDto.setRole("ADMIN");

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(userBanDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/users/" + userRequestDto.getId() + "/ban")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .requestAttr("userId", userRequestDto.getId())
                .requestAttr("role", userRequestDto.getRole());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenRequestToUnbanUserEndpointWithValidParameters_thenReturnResponseOk() throws Exception {
        UserBanDto userBanDto = UserBanDtoFixture.load();
        UserRequestDto userRequestDto = UserRequestDtoFixture.load();
        userRequestDto.setId(1L);
        userRequestDto.setRole("ADMIN");

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(userBanDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/users/" + userRequestDto.getId() + "/unban")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .requestAttr("userId", userRequestDto.getId())
                .requestAttr("role", userRequestDto.getRole());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());
    }

    @Test
    void givenRequestToUnbanUserEndpointWithInvalidParameters_thenReturnBadRequest() throws Exception {
        UserBanDto userBanDto = UserBanDtoFixture.load();
        userBanDto.setMotive(null);

        UserRequestDto userRequestDto = UserRequestDtoFixture.load();
        userRequestDto.setId(1L);
        userRequestDto.setRole("ADMIN");

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(userBanDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/users/" + userRequestDto.getId() + "/unban")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .requestAttr("userId", userRequestDto.getId())
                .requestAttr("role", userRequestDto.getRole());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenRequestToResetEmailRequestEndpointWithValidParameters_thenReturnResponseOk() throws Exception {
        UserRequestDto userRequestDto = UserRequestDtoFixture.load();
        userRequestDto.setId(1L);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(userRequestDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/request-email-change")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .requestAttr("userId", userRequestDto.getId());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    void givenRequestToResetEmailRequestEndpointWithInvalidParameters_thenReturnBadRequest() throws Exception {
        UserRequestDto userRequestDto = UserRequestDtoFixture.load();
        userRequestDto.setEmail(null);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(userRequestDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/request-email-change")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenRequestToChangePasswordEndpointWithValidParameters_thenReturnResponseOk() throws Exception {
        ChangePasswordDto changePasswordDto = ChangePasswordDtoFixture.load();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(changePasswordDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .requestAttr("userId", 1L);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    void givenRequestToChangePasswordEndpointWithInvalidParameters_thenReturnBadRequest() throws Exception {
        ChangePasswordDto changePasswordDto = ChangePasswordDtoFixture.load();
        changePasswordDto.setPassword(null);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(changePasswordDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .requestAttr("userId", 1L);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }
}
