package com.p2p.oms.user.service;

import com.p2p.oms.exception.NotFoundException;
import com.p2p.oms.user.entity.User;
import com.p2p.oms.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User createUser(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("Пользователь с email " + email + " уже существует");
        }
        User user = User.create(email);
        return userRepository.save(user);
    }

    @Transactional
    public void deposit(UUID userId, BigDecimal amount) {
        User user = getUserOrThrow(userId);
        user.deposit(amount);
    }

    @Transactional
    public void withdraw(UUID userId, BigDecimal amount) {
        User user = getUserOrThrow(userId);
        user.withdraw(amount);
    }

    public User getUser(UUID userId) {
        return getUserOrThrow(userId);
    }

    private User getUserOrThrow(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(NotFoundException.of("Пользователь с id " + userId + " не найден"));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(NotFoundException.of("Пользователь с email " + email + " не найден"));
    }
}