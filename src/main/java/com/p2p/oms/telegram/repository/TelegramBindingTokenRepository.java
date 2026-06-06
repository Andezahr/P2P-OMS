package com.p2p.oms.telegram.repository;

import com.p2p.oms.telegram.entity.TelegramBindingToken;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

@NullMarked
public interface TelegramBindingTokenRepository extends JpaRepository<TelegramBindingToken, UUID> {
    Optional<TelegramBindingToken> findByToken(String token);
}