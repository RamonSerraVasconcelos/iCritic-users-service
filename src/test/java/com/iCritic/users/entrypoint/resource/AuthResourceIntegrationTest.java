package com.iCritic.users.entrypoint.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iCritic.users.core.fixture.UserFixture;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.CreateUserUseCase;
import com.iCritic.users.core.usecase.FindUserByIdUseCase;
import com.iCritic.users.core.usecase.SignInUserUseCase;
import com.iCritic.users.entrypoint.fixture.AuthorizationDataFixture;
import com.iCritic.users.entrypoint.fixture.UserRequestDtoFixture;
import com.iCritic.users.entrypoint.model.AuthorizationData;
import com.iCritic.users.entrypoint.model.UserRequestDto;
import com.iCritic.users.entrypoint.validation.AuthorizationFilter;
import com.iCritic.users.dataprovider.jwt.JwtProvider;
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

    @MockBean
    private FindUserByIdUseCase findUserByIdUseCase;

    @MockBean
    private JwtProvider jwtProvider;

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
                .andExpect(jsonPath("$.createdAt").isNotEmpty());


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
        User loggedUser = UserFixture.load();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(userRequestDto);

        when(signInUserUseCase.execute(any(User.class))).thenReturn(loggedUser);
        when(jwtProvider.generateToken(any())).thenReturn(authorizationData.getAccessToken());
        when(jwtProvider.generateRefreshToken(any())).thenReturn(authorizationData.getRefreshToken());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(authorizationData.getAccessToken()))
                .andExpect(jsonPath("$.refreshToken").value(authorizationData.getRefreshToken()));
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
