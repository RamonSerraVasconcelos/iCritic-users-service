package com.iCritic.iCritic.dataprovider.gateway.database.repository;

import com.iCritic.iCritic.core.enums.BanActionEnum;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class UpdateUserBanListRepository {

    private final JdbcTemplate jdbcTemplate;

    public void updateBanList(Long userId, String motive, String action) {
        String query = "INSERT INTO banlist(user_id, motive, action) VALUES (?, ?, ?)";
        jdbcTemplate.update(query, userId, motive, action);
    }
}
