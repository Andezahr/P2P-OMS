package com.p2p.oms.telegram.repository;

import com.p2p.oms.telegram.entity.TelegramBinding;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

@NullMarked
public interface TelegramBindingRepository extends JpaRepository<TelegramBinding, UUID> {
    Optional<TelegramBinding> findByChatId(String chatId);
    Optional<TelegramBinding> findByUserId(UUID userId);
    boolean existsByUserId(UUID userId);
}