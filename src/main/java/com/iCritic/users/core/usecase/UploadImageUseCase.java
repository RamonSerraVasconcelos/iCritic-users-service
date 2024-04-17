package com.iCritic.users.core.usecase;

import com.iCritic.users.core.model.Image;
import com.iCritic.users.core.usecase.boundary.SaveImageInfoBoundary;
import com.iCritic.users.core.usecase.boundary.UploadImageBoundary;
import com.iCritic.users.exception.ResourceViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UploadImageUseCase {

    private final UploadImageBoundary uploadImageBoundary;

    private final SaveImageInfoBoundary saveImageInfoBoundary;

    public Image execute(InputStream file, String originalFileName) {
        try {
            String extension = getExtension(originalFileName);

            if (!isImageExtensionValid(extension)) {
                throw new ResourceViolationException("Invalid image extension");
            }

            Image image = Image.builder()
                    .file(file)
                    .name(generateFileName(extension))
                    .originalName(originalFileName)
                    .extension(extension)
                    .build();

            uploadImageBoundary.execute(image);
            return saveImageInfoBoundary.execute(image);
        } catch (Exception e) {
            log.error("Error while uploading image:", e);
            throw e;
        }
    }

    private String generateFileName(String extension) {
        return "thumb_" + String.valueOf(System.currentTimeMillis()) + extension;
    }

    private String getExtension(String originalFileName) {
        return originalFileName.substring(originalFileName.lastIndexOf('.'));
    }

    private boolean isImageExtensionValid(String extension) {
        List<String> validExtensions = Arrays.asList(".jpg", ".jpeg", ".png");

        return validExtensions.contains(extension.toLowerCase());
    }
}
