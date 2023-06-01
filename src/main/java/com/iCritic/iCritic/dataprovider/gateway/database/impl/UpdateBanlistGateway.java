package com.iCritic.iCritic.dataprovider.gateway.database.impl;

import com.iCritic.iCritic.core.enums.BanActionEnum;
import com.iCritic.iCritic.core.usecase.boundary.UpdateBanListBoundary;
import com.iCritic.iCritic.dataprovider.gateway.database.repository.UpdateUserBanListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UpdateBanlistGateway implements UpdateBanListBoundary {

    private final UpdateUserBanListRepository updateUserBanListRepository;

    public void execute(Long userId, String motive, String action) {
        updateUserBanListRepository.updateBanList(userId, motive, action);
    }
}
