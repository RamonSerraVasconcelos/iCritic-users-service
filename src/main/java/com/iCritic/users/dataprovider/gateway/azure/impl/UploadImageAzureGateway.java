package com.iCritic.users.dataprovider.gateway.azure.impl;

import com.azure.storage.blob.BlobClient;
import com.iCritic.users.config.AzureStorageConfig;
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

    public void execute(Image image) {
        try {
            BlobClient blobClient = azureStorageConfig.getBlobClient(image.getName());

            byte[] content = FileCopyUtils.copyToByteArray(image.getFile());
            ByteArrayInputStream inputStream = new ByteArrayInputStream(content);

            blobClient.upload(inputStream, content.length);
        } catch (Exception e) {
            log.error("Error while uploading image:", e);
        }
    }
}
