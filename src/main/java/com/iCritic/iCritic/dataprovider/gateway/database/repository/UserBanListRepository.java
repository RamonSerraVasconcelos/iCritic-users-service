package com.iCritic.iCritic.dataprovider.gateway.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class UserBanListRepository {

    private final JdbcTemplate jdbcTemplate;

    public void updateBanList(Long userId, String motive, String action) {
        String query = "INSERT INTO banlist(user_id, motive, action) VALUES (?, ?, ?)";
        jdbcTemplate.update(query, userId, motive, action);
    }
}
