package com.p2p.oms.user.dto;

import com.p2p.oms.user.entity.User;

import java.math.BigDecimal;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        BigDecimal balance,
        BigDecimal availableBalance
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getBalance(),
                user.availableBalance()
        );
    }
}
