package com.iCritic.iCritic.core.user.repository;

import com.iCritic.iCritic.core.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
