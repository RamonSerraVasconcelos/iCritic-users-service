package com.iCritic.iCritic.core.model;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Country {

    private Long id;
    private String name;
}
