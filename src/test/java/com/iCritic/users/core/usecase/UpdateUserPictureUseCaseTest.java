package com.iCritic.users.core.usecase;

import com.iCritic.users.core.fixture.ImageFixture;
import com.iCritic.users.core.fixture.UserFixture;
import com.iCritic.users.core.model.Image;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.FindUserByIdBoundary;
import com.iCritic.users.core.usecase.boundary.UpdateUserBoundary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateUserPictureUseCaseTest {

    @InjectMocks
    private UpdateUserPictureUseCase updateUserPictureUseCase;

    @Mock
    private FindUserByIdBoundary findUserByIdBoundary;

    @Mock
    private UploadImageUseCase uploadImageUseCase;

    @Mock
    private UpdateUserBoundary updateUserBoundary;

    @Test
    void givenValidParameters_whenUserIsFound_thenCallBoundarys() {
        User user = UserFixture.load();
        Image image = ImageFixture.load();

        String originalFileName = "testFile.png";
        InputStream file = new ByteArrayInputStream(new byte[0]);

        when(findUserByIdBoundary.execute(user.getId())).thenReturn(user);
        when(uploadImageUseCase.execute(file, originalFileName)).thenReturn(image);
        when(updateUserBoundary.execute(user)).thenReturn(user);

        updateUserPictureUseCase.execute(user.getId(), originalFileName, file);

        verify(findUserByIdBoundary).execute(user.getId());
        verify(uploadImageUseCase).execute(file, originalFileName);
        verify(updateUserBoundary).execute(user);
    }
}
