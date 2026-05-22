package com.p2p.oms.exception;

import lombok.Getter;

public abstract class ApiException extends RuntimeException {

    @Getter
    private final String code;

    protected ApiException(String code, String message) {
        super(message);
        this.code = code;
    }
}