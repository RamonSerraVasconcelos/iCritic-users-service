package com.iCritic.users.dataprovider.gateway.database.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "images")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    private String name;

    private String originalName;

    private String extension;
}
