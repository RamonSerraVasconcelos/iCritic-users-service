package com.iCritic.users.dataprovider.azure.impl;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.iCritic.users.config.AzureStorageConfig;
import com.iCritic.users.core.fixture.ImageFixture;
import com.iCritic.users.core.model.Image;
import com.iCritic.users.dataprovider.gateway.azure.impl.UploadImageAzureGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UploadImageAzureGatewayTest {

    @InjectMocks
    private UploadImageAzureGateway uploadImageAzureGateway;

    @Mock
    private AzureStorageConfig azureStorageConfig;

    @Mock
    private BlobClient blobClient;

    @Test
    void givenImage_thenGetBlobClientAndCallUpload() throws IOException {
        Image image = ImageFixture.load();

        when(azureStorageConfig.getBlobClient(image.getName())).thenReturn(blobClient);

        uploadImageAzureGateway.execute(image);

        verify(azureStorageConfig).getBlobClient(image.getName());
        verify(blobClient).upload(any(InputStream.class), anyLong());
    }
}
