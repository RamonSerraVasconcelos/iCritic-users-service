package com.iCritic.users.dataprovider.gateway.kafka.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EmailNotificationMessage {

    private Long userId;
    private String notificationSubjectId;
    private String email;
    private String subject;
    private String body;
}
