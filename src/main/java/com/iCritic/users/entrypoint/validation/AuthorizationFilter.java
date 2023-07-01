package com.iCritic.users.entrypoint.validation;

import static java.util.Objects.isNull;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.iCritic.users.dataprovider.jwt.JwtProvider;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

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
            log.info("Retrieving information from access token id: [{}]", jwtProvider.getTokenId(token));
            request.setAttribute("userId", jwtProvider.getUserIdFromToken(token));
            request.setAttribute("role", jwtProvider.getUserRoleFromToken(token));
        } catch (Exception e) {
            log.error("Error retrieving claims from access token: [{}]", e.getMessage());
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
