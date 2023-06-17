package com.iCritic.users.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.application.properties")
@Getter
@Setter
public class ApplicationProperties {

    private int jwtExpiration;
    private int jwtRefreshExpiration;
    private String jwtSecret;
    private String jwtRefreshSecret;
}
