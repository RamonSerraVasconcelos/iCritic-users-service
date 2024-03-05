package com.iCritic.users.dataprovider.gateway.database.repository;

import com.iCritic.users.dataprovider.gateway.database.entity.CountryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<CountryEntity, Long> {

    Page<CountryEntity> findAllByOrderByIdAsc(Pageable pageable);
}
