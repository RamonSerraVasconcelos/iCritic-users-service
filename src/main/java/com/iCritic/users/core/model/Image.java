package com.iCritic.users.core.model;

import lombok.*;

import java.io.InputStream;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Image {

    private Long id;
    private String name;
    private InputStream file;
    private String originalName;
    private String extension;
}
