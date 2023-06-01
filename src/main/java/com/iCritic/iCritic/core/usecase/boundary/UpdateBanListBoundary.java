package com.iCritic.iCritic.core.usecase.boundary;

import com.iCritic.iCritic.core.enums.BanActionEnum;

public interface UpdateBanListBoundary {

    void execute(Long userId, String motive, String action);
}
