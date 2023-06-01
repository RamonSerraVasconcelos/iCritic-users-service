package com.iCritic.iCritic.dataprovider.gateway.database.repository;

import com.iCritic.iCritic.core.enums.Role;
import com.iCritic.iCritic.dataprovider.gateway.database.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity SET role = :role WHERE id = :id")
    void updateRole(@Param("id") Long id, @Param("role") Role role);

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity set active = :active WHERE id = :id")
    void updateStatus(@Param("id") Long id, @Param("active") boolean active);

    UserEntity findByEmail(String email);
}
