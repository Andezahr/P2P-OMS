package com.p2p.oms.telegram.entity;

import com.p2p.oms.common.entity.BaseEntity;
import com.p2p.oms.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "telegram_bindings",
        indexes = {
                @Index(name = "idx_telegram_bindings_chat_id", columnList = "chatId", unique = true),
                @Index(name = "idx_telegram_bindings_user_id", columnList = "user_id", unique = true)
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TelegramBinding extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String chatId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private TelegramBinding(String chatId, User user) {
        this.chatId = chatId;
        this.user = user;
    }

    public static TelegramBinding create(String chatId, User user) {
        return new TelegramBinding(chatId, user);
    }
}