package com.iCritic.users.dataprovider.gateway.database.mapper;

import com.iCritic.users.core.model.Image;
import com.iCritic.users.dataprovider.gateway.database.entity.ImageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class ImageEntityMapper {

    public static final ImageEntityMapper INSTANCE = Mappers.getMapper(ImageEntityMapper.class);

    public abstract ImageEntity imageToImageEntity(Image image);

    public abstract Image imageEntityToImage(ImageEntity imageEntity);
}
