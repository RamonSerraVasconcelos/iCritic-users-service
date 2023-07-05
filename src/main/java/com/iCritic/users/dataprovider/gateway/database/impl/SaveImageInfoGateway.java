package com.iCritic.users.dataprovider.gateway.database.impl;

import com.iCritic.users.core.model.Image;
import com.iCritic.users.core.usecase.boundary.SaveImageInfoBoundary;
import com.iCritic.users.dataprovider.gateway.database.entity.ImageEntity;
import com.iCritic.users.dataprovider.gateway.database.mapper.ImageEntityMapper;
import com.iCritic.users.dataprovider.gateway.database.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SaveImageInfoGateway implements SaveImageInfoBoundary {

    private final ImageRepository imageRepository;

    public Image execute(Image image) {
        ImageEntityMapper mapper = ImageEntityMapper.INSTANCE;
        ImageEntity imageEntity = mapper.imageToImageEntity(image);

        return mapper.imageEntityToImage(imageRepository.save(imageEntity));
    }
}
