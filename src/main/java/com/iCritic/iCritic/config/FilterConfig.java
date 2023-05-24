package com.iCritic.iCritic.config;

import com.iCritic.iCritic.infrastructure.security.JwtFilter;
import com.iCritic.iCritic.infrastructure.security.JwtGenerator;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class FilterConfig {

    private final JwtGenerator jwtGenerator;

    @Bean
    public FilterRegistrationBean<JwtFilter> authenticationFilter() {

        FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new JwtFilter(jwtGenerator));
        registrationBean.addUrlPatterns("*");
        registrationBean.addInitParameter("excludeUrls", "/register,/login,/refresh,/logout,/forgot-password,/reset-password");

        return registrationBean;
    }
}
