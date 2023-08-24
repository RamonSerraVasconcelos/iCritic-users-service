package com.iCritic.users.entrypoint.validation;

import com.iCritic.users.core.model.AccessToken;
import com.iCritic.users.core.model.Claim;
import com.iCritic.users.core.usecase.boundary.ValidateAccessTokenBoundary;
import com.iCritic.users.core.utils.TokenUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Objects.isNull;

@Component
@AllArgsConstructor
@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter {

    private final ValidateAccessTokenBoundary validateAccessTokenBoundary;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (isEndpointOpen(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader("Authorization");

        if (isNull(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            AccessToken accessToken = validateAccessTokenBoundary.execute(token);

            log.info("Retrieving information from access token id: [{}]", accessToken.getId());

            Claim userIdClaim = TokenUtils.getClaim(accessToken.getClaims(), "userId");
            String userId = userIdClaim.getValue();

            Claim userRoleClaim = TokenUtils.getClaim(accessToken.getClaims(), "role");
            String role = userRoleClaim.getValue();

            request.setAttribute("userId", userId);
            request.setAttribute("role", role);
        } catch (Exception e) {
            log.error("Error retrieving claims from access token", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isEndpointOpen(HttpServletRequest request) {
        String excludeUrls = getFilterConfig().getInitParameter("excludeUrls");
        String[] excludeUrlArray = excludeUrls.split(",");

        for (String excludeUrl : excludeUrlArray) {
            if (request.getRequestURI().matches(excludeUrl.trim())) {
                return true;
            }
        }

        return false;
    }
}
