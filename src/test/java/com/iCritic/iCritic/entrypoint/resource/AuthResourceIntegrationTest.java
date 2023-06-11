package com.iCritic.iCritic.entrypoint.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iCritic.iCritic.core.fixture.UserFixture;
import com.iCritic.iCritic.core.model.User;
import com.iCritic.iCritic.core.usecase.CreateUserUseCase;
import com.iCritic.iCritic.core.usecase.SignInUserUseCase;
import com.iCritic.iCritic.entrypoint.fixture.AuthorizationDataFixture;
import com.iCritic.iCritic.entrypoint.fixture.UserRequestDtoFixture;
import com.iCritic.iCritic.entrypoint.model.AuthorizationData;
import com.iCritic.iCritic.entrypoint.model.UserRequestDto;
import com.iCritic.iCritic.entrypoint.validation.AuthorizationFilter;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = AuthResource.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthResourceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AuthorizationFilter authorizationFilter;

    @MockBean
    private CreateUserUseCase createUserUseCase;

    @MockBean
    private SignInUserUseCase signInUserUseCase;

    @Test
    void givenRequestToRegisterEndpointWithValidParams_thenRegisterAndReturnUser() throws Exception {
        UserRequestDto userRequestDto = UserRequestDtoFixture.load();
        userRequestDto.setPassword("password");
        User createdUser = UserFixture.load();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(userRequestDto);

        when(createUserUseCase.execute(any(User.class))).thenReturn(createdUser);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdUser.getId()))
                .andExpect(jsonPath("$.name").value(createdUser.getName()))
                .andExpect(jsonPath("$.email").value(createdUser.getEmail()))
                .andExpect(jsonPath("$.description").value(createdUser.getDescription()))
                .andExpect(jsonPath("$.active").value(createdUser.isActive()))
                .andExpect(jsonPath("$.role").value(createdUser.getRole().toString()))
                .andExpect(jsonPath("$.country.id").value(createdUser.getCountry().getId()))
                .andExpect(jsonPath("$.country.name").value(createdUser.getCountry().getName()))
                .andExpect(jsonPath("$.createdAt").value(createdUser.getCreatedAt().toString()));


        verify(createUserUseCase).execute(any(User.class));
    }

    @Test
    void givenRequestToRegisterEndpointWithInvalidParams_thenReturnBadRequest() throws Exception {
        UserRequestDto userRequestDto = UserRequestDto.builder().description("test").build();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(userRequestDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenRequestToSignInEndpointWithValidParams_thenSignInAndReturnAccessToken() throws Exception {
        UserRequestDto userRequestDto = UserRequestDto.builder().email("test@test.test").password("password").build();
        AuthorizationData authorizationData = AuthorizationDataFixture.load();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(userRequestDto);

        when(signInUserUseCase.execute(any(User.class))).thenReturn(authorizationData);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(authorizationData.getAccessToken()));
    }

    @Test
    void givenRequestToSignInEndpointWithInvalidParams_thenReturnBadRequest() throws Exception {
        UserRequestDto userRequestDto = UserRequestDto.builder().email("test@test.test").build();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(userRequestDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }
}
