package com.iCritic.users.dataprovider.gateway.database.repository;

import com.iCritic.users.dataprovider.gateway.database.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
}
