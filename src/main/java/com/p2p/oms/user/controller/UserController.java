package com.p2p.oms.user.controller;

import com.p2p.oms.user.dto.BalanceRequest;
import com.p2p.oms.user.dto.UserCreateRequest;
import com.p2p.oms.user.dto.UserResponse;
import com.p2p.oms.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@NullMarked
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        return ResponseEntity.ok(UserResponse.from(userService.createUser(request.email())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID id) {
        return ResponseEntity.ok(UserResponse.from(userService.getUser(id)));
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<Void> deposit(@PathVariable UUID id, @Valid @RequestBody BalanceRequest request) {
        userService.deposit(id, request.amount());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<Void> withdraw(@PathVariable UUID id, @Valid @RequestBody BalanceRequest request) {
        userService.withdraw(id, request.amount());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<UserResponse> getUserByEmail(@RequestParam String email) {
        return ResponseEntity.ok(UserResponse.from(userService.getUserByEmail(email)));
    }

}