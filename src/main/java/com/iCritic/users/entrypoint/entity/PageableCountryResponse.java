package com.iCritic.users.entrypoint.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PageableCountryResponse {

    private List<CountryResponseDto> data;
    private Metadata metadata;
}
