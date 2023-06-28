package com.iCritic.users.core.usecase.boundary;

import com.iCritic.users.core.model.Image;

public interface UploadImageBoundary {

    void execute(Image image);
}
