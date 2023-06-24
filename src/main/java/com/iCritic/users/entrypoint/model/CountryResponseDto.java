package com.iCritic.users.entrypoint.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CountryResponseDto {
    private Long id;

    private String name;
}
