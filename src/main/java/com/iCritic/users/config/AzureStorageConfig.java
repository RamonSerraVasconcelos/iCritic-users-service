package com.iCritic.users.config;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.iCritic.users.config.properties.AzureStorageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureStorageConfig {

    @Autowired
    private AzureStorageProperties azureStorageProperties;

    @Bean
    public BlobServiceClient getBlobServiceClient() {
        return new BlobServiceClientBuilder().connectionString(azureStorageProperties.getConnectionString()).buildClient();
    }
}
