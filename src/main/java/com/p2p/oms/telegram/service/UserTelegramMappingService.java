package com.p2p.oms.telegram.service;

import com.p2p.oms.telegram.entity.TelegramBinding;
import com.p2p.oms.telegram.repository.TelegramBindingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserTelegramMappingService {

    private final TelegramBindingRepository bindingRepository;

    public String getChatIdByUserId(UUID userId) {
        return bindingRepository.findByUserId(userId)
                .map(TelegramBinding::getChatId)
                .orElse(null);
    }

    public boolean isBound(UUID userId) {
        return bindingRepository.existsByUserId(userId);
    }
}