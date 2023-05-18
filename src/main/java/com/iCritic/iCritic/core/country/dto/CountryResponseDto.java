package com.iCritic.iCritic.core.country.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iCritic.iCritic.core.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CountryResponseDto {
    private Long id;

    private String name;
}
