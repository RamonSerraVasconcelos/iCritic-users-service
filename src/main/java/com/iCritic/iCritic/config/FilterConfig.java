package com.iCritic.iCritic.config;

import com.iCritic.iCritic.entrypoint.validation.AuthorizationFilter;
import com.iCritic.iCritic.entrypoint.validation.JwtGenerator;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class FilterConfig {

    private final JwtGenerator jwtGenerator;

    @Bean
    public FilterRegistrationBean<AuthorizationFilter> authenticationFilter() {

        FilterRegistrationBean<AuthorizationFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new AuthorizationFilter(jwtGenerator));
        registrationBean.addUrlPatterns("*");
        registrationBean.addInitParameter("excludeUrls", "/register,/login,/refresh,/logout,/forgot-password,/reset-password");

        return registrationBean;
    }
}
