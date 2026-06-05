package com.p2p.oms.user.dto;

import jakarta.validation.constraints.Email;

public record UserCreateRequest(@Email String email) {
}
