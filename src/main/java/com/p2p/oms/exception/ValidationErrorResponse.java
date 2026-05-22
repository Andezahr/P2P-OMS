package com.p2p.oms.exception;

import java.time.Instant;
import java.util.Map;

public record ValidationErrorResponse(
        String code,
        Map<String, String> errors,
        Instant timestamp
) {
}