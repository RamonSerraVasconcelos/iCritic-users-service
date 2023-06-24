package com.iCritic.users.config;

import com.iCritic.users.entrypoint.validation.AuthorizationFilter;
import com.iCritic.users.dataprovider.jwt.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class FilterConfig {

    private final JwtProvider jwtProvider;

    @Bean
    public FilterRegistrationBean<AuthorizationFilter> authenticationFilter() {

        FilterRegistrationBean<AuthorizationFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new AuthorizationFilter(jwtProvider));
        registrationBean.addUrlPatterns("*");
        registrationBean.addInitParameter("excludeUrls", "/register,/login,/refresh,/logout,/forgot-password,/reset-password");

        return registrationBean;
    }
}
