package com.iCritic.iCritic.dataprovider.gateway.database.repository;

import com.iCritic.iCritic.dataprovider.gateway.database.entity.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<CountryEntity, Long> {
}
