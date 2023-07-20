package com.iCritic.users.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "spring.application.properties.azure")
@Getter
@Setter
public class AzureStorageProperties {

    private String connectionString;
    private String containerName;
    private String containerHostUrl;
}
