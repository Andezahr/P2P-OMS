package com.p2p.oms.telegram.controller;

import com.p2p.oms.telegram.service.TelegramBindingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/telegram")
@RequiredArgsConstructor
public class TelegramBindingController {

    private final TelegramBindingService bindingService;

    @PostMapping("/link-token")
    public LinkTokenResponse generateToken(@RequestBody LinkTokenRequest request) {
        String token = bindingService.generateLinkTokenByEmail(request.email());
        return new LinkTokenResponse(token);
    }

    public record LinkTokenRequest(String email) {}
    public record LinkTokenResponse(String token) {}
}