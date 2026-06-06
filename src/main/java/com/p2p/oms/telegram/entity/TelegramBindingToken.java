package com.p2p.oms.telegram.entity;

import com.p2p.oms.common.entity.BaseEntity;
import com.p2p.oms.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "telegram_binding_tokens")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TelegramBindingToken extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Instant expiresAt;

    private TelegramBindingToken(String token, User user, Instant expiresAt) {
        this.token = token;
        this.user = user;
        this.expiresAt = expiresAt;
    }

    public static TelegramBindingToken create(User user, String token, Instant expiresAt) {
        return new TelegramBindingToken(token, user, expiresAt);
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }
}