package com.iCritic.users.dataprovider.gateway.database.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.iCritic.users.core.fixture.ImageFixture;
import com.iCritic.users.core.model.Image;
import com.iCritic.users.dataprovider.gateway.database.entity.ImageEntity;
import com.iCritic.users.dataprovider.gateway.database.repository.ImageRepository;

@ExtendWith({ MockitoExtension.class })
public class SaveImageInfoGatewayTest {

    @InjectMocks
    private SaveImageInfoGateway saveImageInfoGateway;

    @Mock
    private ImageRepository imageRepository;

    @Test
    void givenImage_thenCallImageRepository_andReturnImage() {
        Image image = ImageFixture.load();

        ImageEntity imageEntity = ImageEntity.builder()
                .id(1L)
                .name(image.getName())
                .originalName(image.getOriginalName())
                .extension(image.getExtension())
                .build();

        when(imageRepository.save(any())).thenReturn(imageEntity);

        Image result = saveImageInfoGateway.execute(image);

        verify(imageRepository).save(any());

        assertNotNull(result);
        assertEquals(result.getId(), image.getId());
        assertEquals(result.getName(), image.getName());
        assertEquals(result.getOriginalName(), image.getOriginalName());
        assertEquals(result.getExtension(), image.getExtension());
    }
}
