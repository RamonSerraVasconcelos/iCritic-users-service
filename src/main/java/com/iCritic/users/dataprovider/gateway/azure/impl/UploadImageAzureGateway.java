package com.iCritic.users.dataprovider.gateway.azure.impl;

import com.iCritic.users.core.model.Image;
import com.iCritic.users.core.usecase.boundary.UploadImageBoundary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UploadImageAzureGateway implements UploadImageBoundary {

    public void execute(Image image) {
        // TODO
    }
}
