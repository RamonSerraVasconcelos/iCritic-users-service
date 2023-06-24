package com.iCritic.users.dataprovider.gateway.database.impl;

import com.iCritic.users.core.usecase.boundary.UpdateBanListBoundary;
import com.iCritic.users.dataprovider.gateway.database.repository.UserBanListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UpdateBanlistGateway implements UpdateBanListBoundary {

    private final UserBanListRepository userBanListRepository;

    public void execute(Long userId, String motive, String action) {
        userBanListRepository.updateBanList(userId, motive, action);
    }
}
