package com.iCritic.users.core.model;

import com.iCritic.users.core.enums.NotificationContentEnum;
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
    protected NotificationContentEnum notificationContentEnum;
    protected String body;
}
