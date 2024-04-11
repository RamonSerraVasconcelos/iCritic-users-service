package com.iCritic.users.config;

import com.iCritic.users.core.usecase.ValidateAccessTokenUseCase;
import com.iCritic.users.core.usecase.boundary.ValidateAccessTokenBoundary;
import com.iCritic.users.core.usecase.boundary.ValidateDecryptedTokenBoundary;
import com.iCritic.users.entrypoint.validation.AuthorizationFilter;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class FilterConfig {

    private final ValidateDecryptedTokenBoundary validateDecryptedTokenBoundary;

    @Bean
    public FilterRegistrationBean<AuthorizationFilter> authenticationFilter() {

        FilterRegistrationBean<AuthorizationFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new AuthorizationFilter(validateDecryptedTokenBoundary));
        registrationBean.addUrlPatterns("*");
        registrationBean.addInitParameter("excludeUrls", "/register,/login,/refresh,/logout,/forgot-password,/reset-password,/reset-email, /countries, /countries/*, /auth/token/validate");

        return registrationBean;
    }
}
