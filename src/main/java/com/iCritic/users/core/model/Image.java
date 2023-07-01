package com.iCritic.users.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
