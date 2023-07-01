package com.iCritic.users.core.fixture;

import com.iCritic.users.core.model.Image;

import java.io.ByteArrayInputStream;

public class ImageFixture {

    public static Image load() {
        return Image.builder()
                .id(1L)
                .name("thumb_1111111111111.png")
                .originalName("test.png")
                .extension(".png")
                .file(new ByteArrayInputStream(new byte[0]))
                .build();
    }
}
