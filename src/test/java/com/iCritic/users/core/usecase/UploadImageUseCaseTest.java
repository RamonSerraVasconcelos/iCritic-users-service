package com.iCritic.users.core.usecase;

import com.iCritic.users.core.fixture.ImageFixture;
import com.iCritic.users.core.model.Image;
import com.iCritic.users.core.usecase.boundary.SaveImageInfoBoundary;
import com.iCritic.users.core.usecase.boundary.UploadImageBoundary;
import com.iCritic.users.exception.ResourceViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UploadImageUseCaseTest {

    @InjectMocks
    private UploadImageUseCase uploadImageUseCase;

    @Mock
    private UploadImageBoundary uploadImageBoundary;

    @Mock
    private SaveImageInfoBoundary saveImageInfoBoundary;

    @Test
    void givenValidParameters_thenValidateAndReturnImage() {
        Image image = ImageFixture.load();

        doNothing().when(uploadImageBoundary).execute(any());
        when(saveImageInfoBoundary.execute(any())).thenReturn(image);

        Image result = uploadImageUseCase.execute(image.getFile(), image.getOriginalName());

        verify(uploadImageBoundary).execute(any());
        verify(saveImageInfoBoundary).execute(any());

        assertNotNull(result);
        assertEquals(result.getId(), image.getId());
        assertEquals(result.getName(), image.getName());
        assertEquals(result.getOriginalName(), image.getOriginalName());
        assertEquals(result.getExtension(), image.getExtension());
    }

    @Test
    void givenInvalidImageExtension_thenThrowResourceViolationException() {
        InputStream file = new ByteArrayInputStream(new byte[0]);
        String invalidFileName = "test.pdf";

        assertThrows(ResourceViolationException.class, () -> uploadImageUseCase.execute(file, invalidFileName));
    }
}
