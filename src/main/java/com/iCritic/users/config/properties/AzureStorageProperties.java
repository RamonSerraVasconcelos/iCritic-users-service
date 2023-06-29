package com.iCritic.users.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.application.properties.azure")
@Getter
@Setter
public class AzureStorageProperties {

    private String connectionString;
    private String containerName;
}
