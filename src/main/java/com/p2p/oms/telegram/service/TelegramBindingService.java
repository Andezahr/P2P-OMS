package com.p2p.oms.telegram.service;

import com.p2p.oms.exception.NotFoundException;
import com.p2p.oms.telegram.entity.TelegramBinding;
import com.p2p.oms.telegram.entity.TelegramBindingToken;
import com.p2p.oms.telegram.repository.TelegramBindingRepository;
import com.p2p.oms.telegram.repository.TelegramBindingTokenRepository;
import com.p2p.oms.user.entity.User;
import com.p2p.oms.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TelegramBindingService {

    private static final int TOKEN_TTL_MINUTES = 10;

    private final UserRepository userRepository;
    private final TelegramBindingTokenRepository tokenRepository;
    private final TelegramBindingRepository bindingRepository;

    @Transactional
    public String generateLinkTokenByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(NotFoundException.of("USER_NOT_FOUND"));

        return generateLinkToken(user);
    }

    @Transactional
    public String generateLinkToken(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(NotFoundException.of("USER_NOT_FOUND"));

        return generateLinkToken(user);
    }

    private String generateLinkToken(User user) {
        if (bindingRepository.existsByUserId(user.getId())) {
            throw new IllegalStateException("User already has Telegram binding");
        }

        String token = UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
        TelegramBindingToken entity = TelegramBindingToken.create(
                user,
                token,
                Instant.now().plusSeconds(TOKEN_TTL_MINUTES * 60L)
        );
        tokenRepository.save(entity);
        return token;
    }

    @Transactional
    public void bindByToken(String chatId, String token) {
        TelegramBindingToken bindingToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (bindingToken.isExpired()) {
            tokenRepository.delete(bindingToken);
            throw new IllegalStateException("Token expired");
        }

        if (bindingRepository.existsByUserId(bindingToken.getUser().getId())) {
            tokenRepository.delete(bindingToken);
            throw new IllegalStateException("User already bound");
        }

        bindingRepository.findByChatId(chatId)
                .ifPresent(bindingRepository::delete);

        TelegramBinding binding = TelegramBinding.create(chatId, bindingToken.getUser());
        bindingRepository.save(binding);
        tokenRepository.delete(bindingToken);
    }
}