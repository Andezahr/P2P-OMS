package com.p2p.oms.order.dto.request;

import jakarta.validation.constraints.NotBlank;

public record DisputeRequest(@NotBlank String reason) {
}
