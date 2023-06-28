package com.iCritic.users.entrypoint.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iCritic.users.core.fixture.UserFixture;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.*;
import com.iCritic.users.entrypoint.fixture.UserBanDtoFixture;
import com.iCritic.users.entrypoint.fixture.UserRequestDtoFixture;
import com.iCritic.users.entrypoint.model.UserBanDto;
import com.iCritic.users.entrypoint.model.UserRequestDto;
import com.iCritic.users.entrypoint.validation.AuthorizationFilter;
import com.iCritic.users.core.usecase.ValidateUserRoleUseCase;
import com.iCritic.users.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
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

    @Test
    void givenRequestToUsersEndpoint_thenReturnAllUsers() throws Exception {
        List<User> users = List.of(UserFixture.load(), UserFixture.load(), UserFixture.load());

        when(findUsersUseCase.execute()).thenReturn(users);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(users.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(users.get(0).getName()))
                .andExpect(jsonPath("$[0].email").value(users.get(0).getEmail()))
                .andExpect(jsonPath("$[0].description").value(users.get(0).getDescription()))
                .andExpect(jsonPath("$[0].active").value(users.get(0).isActive()))
                .andExpect(jsonPath("$[0].role").value(users.get(0).getRole().toString()))
                .andExpect(jsonPath("$[0].country.id").value(users.get(0).getCountry().getId()))
                .andExpect(jsonPath("$[0].country.name").value(users.get(0).getCountry().getName()))
                .andExpect(jsonPath("$[0].createdAt").isNotEmpty());

        verify(findUsersUseCase).execute();
    }

    @Test
    void givenRequestToGetUserEndpoint_thenReturnUser() throws Exception {
        User user = UserFixture.load();

        when(findUserByIdUseCase.execute(user.getId())).thenReturn(user);

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
        User user = UserFixture.load();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(userRequestDto);

        when(updateUserUseCase.execute(anyLong(), any())).thenReturn(user);

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
                .andExpect(status().isOk());
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
                .andExpect(status().isOk());
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
}
