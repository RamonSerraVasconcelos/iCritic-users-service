package com.iCritic.users.config;

import com.azure.storage.blob.BlobClient;
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

    public BlobClient getBlobClient(String imageName) {
        BlobServiceClient blobServiceClient = getBlobServiceClient();

        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(azureStorageProperties.getContainerName());
        return containerClient.getBlobClient(imageName);
    }
}
