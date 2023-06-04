package com.iCritic.iCritic.entrypoint.validation;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorizationFilterTest {

    @InjectMocks
    private AuthorizationFilter authorizationFilter;

    @Mock
    private JwtGenerator jwtGenerator;

    @Mock
    FilterChain filterChain;

    private MockHttpServletRequest request;

    private MockHttpServletResponse response;

    private final String TOKEN = "A1B2C3D4E5F6G7H8I9J0K1L2M3N4O5P6Q7R8S9T0U1V2W3X4Y5Z6";

    @BeforeEach
    public void setup() throws Exception {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();

        MockFilterConfig filterConfig = new MockFilterConfig();
        filterConfig.addInitParameter("excludeUrls", "/public");

        authorizationFilter.init(filterConfig);
    }

    @Test
    void givenValidToken_whenValidatingToken_thenSetAttributesAndFinishFilter() throws ServletException, IOException {
        String userId = "1";
        String userRole = "DEFAULT";

        when(jwtGenerator.validateToken(TOKEN)).thenReturn(true);
        when(jwtGenerator.getUserIdFromToken(TOKEN)).thenReturn(userId);
        when(jwtGenerator.getUserRoleFromToken(TOKEN)).thenReturn(userRole);

        request.setRequestURI("/private");
        request.addHeader("Authorization", "Bearer " + TOKEN);

        authorizationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertEquals(userId, request.getAttribute("userId"));
        assertEquals(userRole, request.getAttribute("role"));
    }

    @Test
    void givenOpenEndpoint_thenFinishFilter() throws ServletException, IOException {
        request.setRequestURI("/public");

        authorizationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(request.getAttribute("userId"));
        assertNull(request.getAttribute("role"));
    }

    @Test
    void givenNullToken_thenReturnUnauthorizedResponse() throws ServletException, IOException {
        request.setRequestURI("/private");

        authorizationFilter.doFilterInternal(request, response, filterChain);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        verifyNoInteractions(filterChain);
    }

    @Test
    void givenInvalidToken_thenReturnUnauthorizedResponse() throws ServletException, IOException {
        when(jwtGenerator.validateToken(TOKEN)).thenReturn(false);

        request.setRequestURI("/private");
        request.addHeader("Authorization", "Bearer " + TOKEN);

        authorizationFilter.doFilterInternal(request, response, filterChain);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        verifyNoInteractions(filterChain);
    }
}
