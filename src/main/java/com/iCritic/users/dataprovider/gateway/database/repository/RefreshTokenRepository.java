package com.iCritic.users.dataprovider.gateway.database.repository;

import com.iCritic.users.dataprovider.gateway.database.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, String> {

    @Modifying
    @Transactional
    @Query("UPDATE RefreshTokenEntity SET active = false WHERE id = :id")
    void revokeToken(@Param("id") String userId);

    @Modifying
    @Transactional
    @Query("UPDATE RefreshTokenEntity SET active = false WHERE user_id = :userId AND active = true")
    void revokeUserTokens(@Param("userId") Long userId);
}
