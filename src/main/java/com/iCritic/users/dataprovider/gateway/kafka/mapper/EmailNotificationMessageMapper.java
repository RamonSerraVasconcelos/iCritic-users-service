package com.iCritic.users.dataprovider.gateway.kafka.mapper;

import com.iCritic.users.core.model.EmailNotification;
import com.iCritic.users.dataprovider.gateway.kafka.entity.EmailNotificationMessage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class EmailNotificationMessageMapper {

    public static EmailNotificationMessageMapper INSTANCE = Mappers.getMapper(EmailNotificationMessageMapper.class);

    public abstract EmailNotificationMessage emailNotificationToEmailNotificationMessage(EmailNotification emailNotification);
}
