package com.example.daydreamer.repository;

import com.example.daydreamer.entity.AuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<AuthEntity, String> {
    Optional<AuthEntity> findByPhone(String phone);
    Optional<AuthEntity> findByRefreshToken(String refreshToken);

    Optional<AuthEntity> findByEmail(String email);
}
