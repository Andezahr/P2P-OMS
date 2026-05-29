package com.p2p.oms.exception;

import java.util.function.Supplier;

public class NotFoundException extends ApiException {

    public NotFoundException(String message) {
        super("NOT_FOUND", message);
    }

    public static Supplier<NotFoundException> of(String message) {
        return () -> new NotFoundException(message);
    }
}
