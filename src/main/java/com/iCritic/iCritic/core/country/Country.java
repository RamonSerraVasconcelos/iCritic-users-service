package com.iCritic.iCritic.core.country;

import com.iCritic.iCritic.core.user.User;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "countries")
@Getter
public class Country {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String name;
}
