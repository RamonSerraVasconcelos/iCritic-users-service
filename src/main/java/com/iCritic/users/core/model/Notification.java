package com.iCritic.users.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public abstract class Notification {

    protected Long userId;
    protected String notificationSubjectId;
    protected String subject;
    protected String body;
}
