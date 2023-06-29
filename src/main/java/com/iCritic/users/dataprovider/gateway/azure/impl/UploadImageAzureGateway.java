package com.iCritic.users.dataprovider.gateway.azure.impl;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.iCritic.users.config.AzureStorageConfig;
import com.iCritic.users.config.properties.AzureStorageProperties;
import com.iCritic.users.core.model.Image;
import com.iCritic.users.core.usecase.boundary.UploadImageBoundary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.ByteArrayInputStream;

@Component
@Slf4j
public class UploadImageAzureGateway implements UploadImageBoundary {

    @Autowired
    private AzureStorageConfig azureStorageConfig;

    @Autowired
    private AzureStorageProperties azureStorageProperties;

    public void execute(Image image) {
        try {
            byte[] content = FileCopyUtils.copyToByteArray(image.getFile());
            BlobServiceClient blobServiceClient = azureStorageConfig.getBlobServiceClient();

            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(azureStorageProperties.getContainerName());
            BlobClient blobClient = containerClient.getBlobClient(image.getName());

            ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
            blobClient.upload(inputStream, content.length);
        } catch (Exception e) {
            log.error("Error while uploading image:", e);
        }
    }
}
