package com.iCritic.users.config;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.iCritic.users.config.properties.AzureStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureStorageConfig {

    @Autowired
    private AzureStorageProperties azureStorageProperties;

    private BlobServiceClient getBlobServiceClient() {
        return new BlobServiceClientBuilder().connectionString(azureStorageProperties.getConnectionString()).buildClient();
    }

    public BlobContainerClient getPicturesBlobContainerClient() {
        BlobServiceClient blobServiceClient = getBlobServiceClient();

        return blobServiceClient.getBlobContainerClient(azureStorageProperties.getContainerName());
    }
}
