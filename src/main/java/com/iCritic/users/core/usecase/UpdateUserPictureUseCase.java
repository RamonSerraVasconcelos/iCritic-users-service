package com.iCritic.users.core.usecase;

import com.iCritic.users.core.model.Image;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.FindUserByIdBoundary;
import com.iCritic.users.core.usecase.boundary.UpdateUserBoundary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class UpdateUserPictureUseCase {

    private final FindUserByIdBoundary findUserByIdBoundary;

    private final UploadImageUseCase uploadImageUseCase;

    private final UpdateUserBoundary updateUserBoundary;

    public void execute(Long userId, String originalFileName, InputStream file) {
        User user = findUserByIdBoundary.execute(userId);

        Image image = uploadImageUseCase.execute(file, originalFileName);

        user.setProfilePicture(image);

        updateUserBoundary.execute(user);
    }
}
