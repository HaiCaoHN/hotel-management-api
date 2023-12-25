package com.example.karmashopbe.repositories;

import com.example.karmashopbe.models.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {
    Optional<RefreshToken> findRefreshTokenByRefreshToken(String token);

    RefreshToken findRefreshTokenByUserEmail(String email);
}
