package com.p2p.oms.exception;

import java.util.function.Supplier;

public class CriticalLogicException extends RuntimeException {
    public CriticalLogicException(String message) {
        super(message);
    }

    public Supplier<CriticalLogicException> of(String message) {
        return () -> new CriticalLogicException(message);
    }
}
