package com.iCritic.iCritic.dataprovider.gateway.database.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "countries")
@Getter
@Setter
@Builder
public class CountryEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String name;
}
